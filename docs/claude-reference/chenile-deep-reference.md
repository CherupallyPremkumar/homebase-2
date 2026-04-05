# Chenile Framework Deep Reference

> This file contains detailed Chenile framework code examples and patterns.  
> Read this when implementing new modules, STM actions, controllers, or entity stores.

## Core Architecture: The Interceptor Chain

Every HTTP request flows through Chenile's processing pipeline:

```
HTTP Request → ControllerSupport.process()
  → ChenileExchangeBuilder (builds ChenileExchange)
  → ChenileEntryPoint (orchestrates the pipeline)
    → PopulateContextContainer (fills ContextContainer with headers/tenant/user)
    → ValidateCopyHeaders (validates required headers)
    → SecurityInterceptor (checks @SecurityConfig ACLs)
    → BaseChenileInterceptor subclasses (currency, region, custom interceptors)
    → ServiceInvoker (invokes the actual service method)
  → GenericResponseBuilder (wraps response in GenericResponse<T>)
→ HTTP Response
```

**Key classes:**
- `ChenileExchange` — the request/response context object passed through the entire chain. Contains headers, body, bodyType, response, exception, serviceDefinition, operationDefinition.
- `ContextContainer` — thread-local singleton (`ContextContainer.getInstance()`) holding per-request context: tenant, userId, region, trajectory, authentication. Populated by `PopulateContextContainer` interceptor.
- `ChenileEntryPoint` — the orchestrator that runs the interceptor chain via `owiz` (Chenile's internal orchestration framework).
- `GenericResponse<T>` — standard response wrapper with `success`, `data`, `errors[]`, `code`, `description`, `severity`, `warningMessages`.

## STM (State Transition Machine) — The Heart of Chenile

STM manages entity lifecycles. Every stateful entity (Order, User, Payment, etc.) has an STM that defines valid states, transitions, and the actions triggered during transitions.

### STM Class Hierarchy

```
StateEntity (interface)
  └── getCurrentState() / setCurrentState()

ExtendedStateEntity extends StateEntity, ChenileEntity
  └── + stateEntryTime, slaLate, slaTendingLate

AbstractExtendedStateEntity (domain model base class — NO JPA)
  └── extends BaseEntity implements ExtendedStateEntity
  └── BaseEntity: id, createdTime, lastModifiedTime, createdBy, lastModifiedBy, version

AbstractJpaStateEntity (JPA entity base class — WITH JPA annotations)
  └── extends BaseJpaEntity implements ExtendedStateEntity
  └── BaseJpaEntity: id, createdTime, lastModifiedTime, createdBy, tenant, testEntity, version
  └── + slaYellowDate, slaRedDate
```

### STM Core Interfaces

**`STM<T extends StateEntity>`** — the state machine engine:
- `proceed(entity)` — trigger next transition
- `proceed(entity, payload)` — trigger with payload
- `proceed(entity, eventId, payload)` — trigger specific event with payload
- Implementation: `STMImpl`

**`STMFlowStore`** (impl: `STMFlowStoreImpl`) — stores all flow definitions:
- Loaded from XML via `XmlFlowReader`
- Contains: flows, states, transitions, actions, entry/exit actions
- `getStateInfo(state)` — get state descriptor
- `getTransitionAction(transition)` — get the action for a transition
- `getEntryAction(state)` / `getExitAction(state)` — get state entry/exit actions
- `getInitialState()` — get the starting state of a flow

**`STMActionsInfoProvider`** — provides metadata about allowed actions:
- `getAllowedActions(state)` — list of event IDs allowed from this state
- `getAllowedActionsAndMetadata(state)` — events + metadata (ACLs, body types)
- `getMetadata(state, eventId)` — get metadata for a specific event
- `getEventInformation(eventId)` — get event-level info (ACL, body type)

### STM Action Types

**`STMAction<T>`** — entry/exit action on states:
```java
void execute(State startState, State endState, T entity)
```
Used for: `GenericEntryAction` (saves entity + invokes post-save hooks), `GenericExitAction`

**`STMTransitionAction<T>`** — the action executed during a state transition:
```java
void doTransition(T entity, Object payload, State fromState, String eventId,
                  State toState, STMInternalTransitionInvoker<?> stm, Transition transition)
```
Two patterns in HomeBase:
1. Direct implementation: `implements STMTransitionAction<T>` — override `doTransition()`
2. Abstract base: `extends AbstractSTMTransitionAction<T, PayloadType>` — override `transitionTo()` (auto-casts payload)

**`STMAutomaticStateComputation<T>`** — for auto-states (decision points):
```java
String execute(T entity)  // returns event ID to auto-trigger
```
Used with `IfAction` (OGNL boolean → true/false event), `SwitchAction` (OGNL → case-matched event), `ScriptAction`

**`StateEntityRetrievalStrategy<T>`** — retrieves the entity before transition:
```java
T retrieve(T entity)               // load from DB by ID
T merge(T incoming, T existing, String eventId)  // merge incoming payload with existing
```
Default: `GenericRetrievalStrategy` which delegates to `EntityStore`

**`PostSaveHook<T>`** — executes AFTER entity is persisted:
```java
void execute(State startState, State endState, T entity, TransientMap transientMap)
```
Used for: publishing domain events, sending notifications, triggering side-effects.
- `DefaultPostSaveHook` resolves the correct hook using `STMTransitionActionResolver`

### STM Bean Resolution (STMTransitionActionResolver)

This is how Chenile finds the right bean for each transition/hook/auto-state:

```
Bean name = {prefix} + {eventId/stateId} + {suffix}

Transition actions:  {prefix}{EventId}Action          (suffix: "Action")
Post-save hooks:     {prefix}{StateId}PostSaveHook    (suffix: "PostSaveHook")
Auto-state actions:  {prefix}{StateId}AutoState        (suffix: "AutoState")
```

Example for Order (prefix = "order"):
- `orderPaymentSucceededAction` — transition action for PAYMENT_SUCCEEDED event
- `orderCREATEDPostSaveHook` — post-save hook when entering CREATED state
- `orderCHECK_CANCELLATION_WINDOWAutoState` — auto-state computation

The resolver uses Spring `ApplicationContext.getBean()` with these computed names. If no bean found, falls back to `defaultAction`.

### STM XML Configuration Format

```xml
<stm>
  <!-- Event definitions with ACLs and payload body types -->
  <event-information>
    <event id="PAYMENT_SUCCEEDED" meta-acl="SYSTEM" meta-bodyType="com.example.PaymentPayload"/>
    <event id="CANCEL" meta-acl="CUSTOMER,ADMIN"/>
  </event-information>

  <flow id="order" initial-state="CREATED">
    <security-strategy componentName="stmSecurityStrategy"/>

    <state id="CREATED" meta-mainPath="true">
      <transition event="PAYMENT_SUCCEEDED" new-state="PAID" componentName="paymentSucceededAction"/>
      <transition event="CANCEL" new-state="CANCELLED"/>
    </state>

    <!-- Auto-state: decision point with OGNL condition -->
    <auto-state id="CHECK_CANCELLATION_WINDOW">
      <transition event="true" new-state="CANCELLATION_APPROVED"
                  componentName="ifAction" condition="cancellationAllowed == true"/>
      <transition event="false" new-state="CANCELLATION_DENIED"/>
    </auto-state>

    <state id="DELIVERED" isFinal="true"/>
  </flow>
</stm>
```

Key XML elements:
- `<flow>` — a named workflow with an initial state
- `<state>` — a normal state with transitions
- `<auto-state>` — a decision state (entity passes through automatically)
- `<transition>` — from current state, on event, go to new-state
- `<event-information>` — defines events with metadata (ACLs, body types)
- `<security-strategy>` — component for ACL checking (uses `STMSecurityStrategy`)
- `meta-*` attributes on states — arbitrary metadata (mainPath, SLA hours, etc.)
- `isFinal="true"` — terminal state

### Full STM Wiring in Configuration

Every BC's `{bc}Configuration.java` must wire the STM chain in this exact order:

```java
@Configuration
public class OrderConfiguration {
    // 1. Bean factory adapter (bridges Spring → Chenile)
    @Bean BeanFactoryAdapter orderBeanFactory(ApplicationContext ctx) { ... }

    // 2. Flow store (loads XML, holds state machine definition)
    @Bean STMFlowStoreImpl orderFlowStore(BeanFactoryAdapter bfa) {
        STMFlowStoreImpl fs = new STMFlowStoreImpl();
        fs.setBeanFactory(bfa);
        new XmlFlowReader(fs).include("classpath:...order-states.xml");
        return fs;
    }

    // 3. STM engine
    @Bean STM<Order> orderEntityStm(STMFlowStoreImpl fs) {
        STMImpl<Order> stm = new STMImpl<>();
        stm.setStmFlowStore(fs);
        return stm;
    }

    // 4. Actions info provider (metadata about allowed actions per state)
    @Bean STMActionsInfoProvider orderActionsInfoProvider(STMFlowStoreImpl fs) {
        return new STMActionsInfoProvider(fs);
    }

    // 5. Transition action resolver (finds beans by naming convention)
    @Bean STMTransitionActionResolver orderResolver() {
        return new STMTransitionActionResolver("order");
    }

    // 6. Entry/exit actions (persist entity, invoke post-save hooks)
    @Bean GenericEntryAction<Order> orderEntryAction(EntityStore<Order> es,
            STMActionsInfoProvider p, DefaultPostSaveHook<Order> psh) {
        return new GenericEntryAction<>(es, p, psh);
    }

    // 7. Entity store (bridges domain repository with Chenile)
    @Bean EntityStore<Order> orderEntityStore(OrderRepository repo, OrderMapper mapper) { ... }

    // 8. State entity service (the main service bean)
    @Bean StateEntityServiceImpl<Order> _orderStateEntityService_(STM<Order> stm,
            STMActionsInfoProvider p, EntityStore<Order> es) {
        return new StateEntityServiceImpl<>(stm, p, es);
    }

    // 9. Individual transition actions (named by convention)
    @Bean STMTransitionAction<Order> orderPaymentSucceededAction() { ... }
    @Bean STMTransitionAction<Order> orderCancelAction() { ... }

    // 10. Post-save hooks (named by convention)
    @Bean PostSaveHook<Order> orderCREATEDPostSaveHook() { ... }

    // 11. Security: authorities supplier for ACL on controller
    @Bean StmAuthoritiesBuilder orderEventAuthoritiesSupplier(STMActionsInfoProvider p) {
        return new StmAuthoritiesBuilder(p, true);
    }
}
```

### StateEntityService — The Service Layer

`StateEntityServiceImpl<T>` is the standard Chenile service for stateful entities:

```java
// Key methods:
create(T entity)                          // Create new entity (triggers initial state)
process(T entity, String eventId, Object payload)  // Trigger event on entity
processById(String id, String eventId, Object payload) // Trigger event by entity ID
retrieve(String id)                       // Load entity + allowed actions
getAllowedActionsAndMetadata(State state)  // What can happen next?
config()                                  // STM flow configuration as Map
```

Response is always `StateEntityServiceResponse<T>` containing:
- `mutatedEntity` — the entity after state change
- `allowedActionsAndMetadata` — list of next possible actions with metadata

### Controller Pattern

Controllers extend `ControllerSupport` and use `@ChenileController`:

```java
@RestController
@ChenileController(value = "orderService", serviceName = "_orderStateEntityService_",
                   healthCheckerName = "orderHealthChecker")
public class OrderController extends ControllerSupport {

    @PostMapping("/order")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Order>>> create(
            HttpServletRequest request, @RequestBody Order order) {
        return process(request, order);  // delegates to ControllerSupport
    }

    @GetMapping("/order/{id}")
    @SecurityConfig(authorities = {"ADMIN", "CUSTOMER"})
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Order>>> retrieve(
            HttpServletRequest request, @PathVariable String id) {
        return process(request, id);
    }

    @PatchMapping("/order/{id}/{eventID}")
    @BodyTypeSelector("orderBodyTypeSelector")
    @SecurityConfig(authoritiesSupplier = "orderEventAuthoritiesSupplier")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Order>>> processById(
            HttpServletRequest request, @PathVariable String id,
            @PathVariable String eventID, @RequestBody Object eventPayload) {
        return process(request, id, eventID, eventPayload);
    }
}
```

Key annotations:
- `@ChenileController(serviceName=...)` — links to the `@Bean` name of the `StateEntityService`
- `@SecurityConfig(authorities={...})` — static ACL list
- `@SecurityConfig(authoritiesSupplier="...")` — dynamic ACL from STM event metadata
- `@BodyTypeSelector("...")` — resolves payload type based on eventId (via `StmBodyTypeSelector`)
- `@ChenileParamType(Foo.class)` — explicitly declare request body type
- `@InterceptedBy({"interceptor1","interceptor2"})` — custom interceptors for this operation

### Entity Hierarchy Pattern

**Domain model** (in `{bc}-domain`):
```java
public class Order extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {
    private List<OrderActivityLog> activities = new ArrayList<>();
    private transient TransientMap transientMap = new TransientMap();
    // Business fields — NO JPA annotations
}
```

**JPA entity** (in `{bc}-infrastructure/persistence/entity/`):
```java
@Entity @Table(name = "orders")
public class OrderEntity extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {
    @OneToMany(cascade = CascadeType.ALL) List<OrderActivityLogEntity> activities;
    @Transient TransientMap transientMap = new TransientMap();
    @Column(name = "...") // all JPA annotations here
}
```

**Mapper** (in `{bc}-infrastructure/persistence/mapper/`):
```java
public class OrderMapper {
    public Order toModel(OrderEntity entity) { ... }
    public OrderEntity toEntity(Order model) { ... }
}
```

**EntityStore** (bridges Chenile with your persistence):
```java
public class ChenileOrderEntityStore extends ChenileJpaEntityStore<Order, OrderEntity> {
    public ChenileOrderEntityStore(OrderJpaRepository repo, OrderMapper mapper) {
        super(repo, entity -> mapper.toModel(entity), model -> mapper.toEntity(model));
    }
}
```

### Workflow Interfaces

**`ActivityEnabledStateEntity`** — enables audit trail on entities:
- `obtainActivities()` — returns the activity log collection
- `addActivity(name, comment)` — adds an activity entry

**`ContainsTransientMap`** — per-request scratchpad, not persisted:
- `getTransientMap()` — returns `TransientMap` (extends `HashMap<String, Object>`)
- Used to pass data from transition actions → post-save hooks
- Also has `previousPayload` field for accessing prior event payload

**`ActivityChecker`** — validates activity completion before transitions:
- `areAllActivitiesComplete(entity)` — checks mandatory activities
- `isMandatoryActivity(entity, eventId)` — check if activity is mandatory
- Activities are defined in STM XML metadata on transitions

### CQRS Query Layer

Query modules use Chenile's query framework with MyBatis:

**Controller** extends `ControllerSupport` with `@ChenileController(serviceName = "searchService")`:
```java
@PostMapping("/order/{queryName}")
public ResponseEntity<GenericResponse<SearchResponse>> search(
    HttpServletRequest request, @PathVariable String queryName,
    @RequestBody SearchRequest<Map<String, Object>> searchRequest) {
    return this.process("search", request, new Object[] { queryName, searchRequest });
}
```

**Query definition** (`{bc}.json`): defines queries, columns, filters, sort, workflow linkage
**MyBatis mapper** (`{bc}.xml`): SQL with dynamic conditions, pagination support

Multi-tenant datasource routing is handled by `multi-datasource-utils` based on `query.defaultTenantId` and `query.datasource's` configuration.

### Pub/Sub (Kafka Integration)

- `ChenilePub` interface — `publish(topic, body, headers)`, `asyncPublish(...)`, `publishToOperation(...)`
- `KafkaPublisher` — implementation using Spring `KafkaTemplate`
- `PubSubEntryPoint` — receives messages and routes to the correct Chenile service operation
- `CustomKafkaConsumer` — auto-subscribes to topics based on `@EventsSubscribedTo` annotations
- Messages carry Chenile headers for context propagation (tenant, user, trajectory)

### Security Model

Three layers of security:

1. **Spring Security** (`ChenileSecurityConfiguration`): Keycloak OAuth2/JWT, multi-tenant JWT decoders per realm
2. **Chenile SecurityInterceptor**: runs in the interceptor chain, calls `SecurityService.doesCurrentUserHaveGuardingAuthorities()`
3. **STM Security** (`StmSecurityStrategyImpl`): checks ACLs defined in STM XML `meta-acl` attributes per event

`StmAuthoritiesBuilder` bridges STM event ACLs to Spring Security — builds an `AuthoritiesSupplier` that reads the current entity state and returns the ACLs for the requested event.

### ContextContainer — Thread-Local Request Context

```java
ContextContainer ctx = ContextContainer.getInstance();
ctx.getTenant();          // current tenant ID
ctx.getUser();            // current user ID
ctx.getRegion();          // region (for currency/locale)
ctx.getTrajectory();      // routing trajectory
ctx.getAuthentication();  // Spring Security Authentication
ctx.get("custom-key");    // arbitrary key-value from headers
ctx.isTestMode();         // test mode flag
```

Domain code must NEVER access `ContextContainer` directly — use port interfaces (e.g., `CurrencyResolver`).

### OWIZ — Orchestration Framework

Chenile uses its own lightweight orchestration library (`owiz`) for interceptor chains:

- `Command<T>` — single unit of work: `execute(T context)`
- `Chain<T>` — sequential list of commands
- `FilterChain<T>` — chain with pre/post processing
- `Router<T>` — routes to different commands based on context (e.g., `TenantRouter`)
- `OrchExecutor<T>` — top-level orchestrator

The main processing pipeline (`chenileHighway`) is a Chain of interceptors configured in `ChenileCoreConfiguration`.

### ID Generation

- `IDGenerator` — pluggable strategies via `IDGenerator.registerStrategy(name, strategy)`
- Default strategy generates UUID-based IDs
- `BaseJpaEntity.initializeIfRequired()` auto-generates ID on persist

### Proxy / Service Registry

- `ProxyBuilder` — creates proxies for service interfaces
- `LocalProxyInvoker` — calls service in-process
- `RemoteProxyInvoker` → `HttpInvoker` — calls service over HTTP
- `ProxyTypeRouter` — routes to local or remote based on service registry

### Tenancy & Multi-Tenant Support

- `TenantSpecificResourceLoader` — loads resources with tenant-specific overrides
- `TenantRouter` — routes commands based on tenant from headers
- `BaseJpaEntity.tenant` field on all JPA entities
- Query datasources configured per tenant in `query.datasource's`

### Chenile Base Exceptions

All exceptions extend `ErrorNumException`:
- `BadRequestException` — 400 errors
- `NotFoundException` — 404 errors
- `ServerException` — 500 errors
- `ConfigurationException` — misconfiguration errors
- Each carries `errorNum`, `subErrorNum`, `params[]`, and `ResponseMessage`

### ConfigProvider / EnablementStrategy

- `ConfigProvider` — provides key-value configuration, used for feature flags
- `ConfigBasedEnablementStrategy` — enables/disables STM states and transitions dynamically
- `ConfigProviderImpl` — default implementation
