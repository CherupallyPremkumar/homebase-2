# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Mandatory Behavior — Read First

**Think like a senior architect and developer on every task.** Before writing or modifying any code:

1. **Understand first.** Read the existing code, understand what's already built, and how it fits the architecture. Never leave dead code behind.
2. **Ask "where does this logic belong?"** Apply SOLID principles, hexagonal architecture, and DDD. Domain logic in domain. Infrastructure in infrastructure. Never mix layers.
3. **Follow Chenile patterns exactly.** All mutations through STM `processById()`, never direct save. All beans via `@Bean` in Configuration — never `@Component`/`@Service`/`@Repository`. Understand the Chenile framework reference below before making changes.
4. **One design for ALL modules.** Every bounded context MUST follow the SAME hexagonal architecture blueprint (see `reference_hexagonal_blueprint.md` in memory). No per-module variations. Domain model → `AbstractExtendedStateEntity` (no JPA). JPA entity → `AbstractJpaStateEntity` (infrastructure only). Same `@Bean` wiring order in every Configuration. Same folder structure. Textbook consistency across all 31+ modules.
4. **Never modify Chenile framework code.** Fix issues on the HomeBase side only.
5. **Java 25 only.** Never downgrade to 17 or any earlier version.
6. **Liquibase only.** All DB migrations in `db-migrations/` module only, never per-module. All test data via Liquibase changelogs, never `schema.sql`.
7. **No stubs in main code.** Stub/mock port beans belong only in test `SpringTestConfig`, never in production `{bc}Configuration`.
8. **No frontend in backend repo.** Never create `-web` modules here.
9. **Check memory** at `~/.claude/projects/-Users-premkumar-HomeBase/memory/` for module plans, design decisions, and feedback before starting work on any module.
10. **Production-grade only.** This system serves 1 lakh+ concurrent users. No shortcuts, no TODO stubs, no "good enough." Every line of code must be production-ready.

## Production Design Standards (Amazon-Level)

Every module, every PR, every line of code must meet these standards. This is NOT a college project.

### API Design
- Every REST endpoint returns `GenericResponse<T>` — never raw objects
- All mutations are **idempotent** — use idempotency keys for POST operations (store in Redis, check before processing)
- All list endpoints support **pagination** (`page`, `size`, `sort`) — never return unbounded results
- Consistent error responses: `errorNum` + `subErrorNum` + human-readable `description`
- All IDs are UUIDs — never auto-increment integers exposed externally
- API versioning via URL path (`/v1/orders`) for breaking changes

### Resilience & Fault Tolerance
- **Circuit breakers** on ALL cross-service calls (Resilience4j already configured for cart, pricing, inventory, shipping, payment, order, promo)
- **Retry with exponential backoff** for transient failures — max 3 retries, 100ms/200ms/400ms
- **Timeouts** on every external call — 5s default, 30s max for batch operations
- **Bulkhead pattern** for thread pool isolation between services
- **Fallback responses** for non-critical failures (e.g., recommendations fail → return empty list, don't break page)
- **Dead Letter Queue (DLQ)** for every Kafka consumer — already configured with 3 retries

### Data Integrity
- **Optimistic locking** via `@Version` on all JPA entities (already in BaseJpaEntity)
- **Database constraints** enforce business rules — not just application code
- **Unique indexes** on business keys (e.g., SKU, email, order number)
- **Foreign keys** for referential integrity — no orphan records
- **Soft delete** via status field — never hard delete production data
- All timestamps in **UTC** — never local timezone

### Performance (1 Lakh+ Concurrent Users)
- **Database indexes** on every column used in WHERE, JOIN, ORDER BY
- **Connection pooling** via HikariCP — max 30 connections per instance (already configured)
- **Redis caching** for: product catalog, pricing rules, user sessions, cart state
- **Kafka for async** — never synchronous cross-service calls in the request path
- **N+1 query prevention** — use `@EntityGraph` or `JOIN FETCH`, never lazy load in loops
- **Pagination everywhere** — DB queries must use LIMIT/OFFSET or keyset pagination
- Query timeout: 5s max per DB query. If it takes longer, it needs an index or redesign

### Testing Standards
- Every STM flow must have **BDD tests for ALL state transitions** — happy path AND error paths
- Every transition action must test: valid transition, invalid state rejection, payload validation
- **No module ships without passing BDD tests** — `mvn test -pl {bc}/{bc}-service` must pass
- Integration tests (`-DperformIt=true`) must pass before any module is marked production-ready
- Test coverage: every public service method, every controller endpoint, every STM event

### Security
- **Authentication required** on all endpoints except health checks
- **Authorization** via STM event ACLs (`meta-acl`) — never skip
- **Input validation** at controller layer — never trust client data
- **SQL injection prevention** — always parameterized queries (MyBatis `#{}`, never `${}`)
- **No secrets in code** — all secrets via environment variables / AWS SSM
- **Audit trail** via `ActivityEnabledStateEntity` on all stateful entities

### Observability
- **Health check** endpoint at `/actuator/health` — must check DB, Redis, Kafka connectivity
- **Prometheus metrics** exposed at `/actuator/prometheus`
- **Structured logging** with correlation IDs (Chenile trajectory)
- Log levels: ERROR for failures needing action, WARN for degraded state, INFO for business events, DEBUG for troubleshooting

## Multi-Agent Coordination Rules

Multiple Claude agents work on this codebase simultaneously. Follow these rules to avoid conflicts:

### Module Ownership During Work
- **One agent per bounded context at a time.** If memory shows another agent is working on `order`, don't touch `order`.
- **Save a memory** when you START working on a module: `project_{bc}_in_progress.md` with date and what you're doing.
- **Delete the in-progress memory** when you finish.
- **Shared files require extra care:** `db-migrations/`, `root-build/build-package/pom.xml`, root `pom.xml` — always read latest before editing.

### Safe Parallel Work Zones
These module groups are independent — agents can work on them simultaneously:
- **Group A:** cart, pricing, promo (cart ecosystem)
- **Group B:** order, payment, checkout (order ecosystem)
- **Group C:** product, offer, inventory (catalog ecosystem)
- **Group D:** shipping, fulfillment, return-processing, returnrequest (logistics)
- **Group E:** user, onboarding, supplier (identity ecosystem)
- **Group F:** notification, support, review (communication)
- **Group G:** settlement, reconciliation, analytics (finance)
- **Group H:** rules-engine, cconfig, policy (platform config)
- **Group I:** cms, catalog (content)

Cross-group dependencies (be careful):
- Cart → calls pricing-client, inventory-client, product-client
- Checkout → calls cart-client, pricing-client, order-client, payment
- Order → publishes events consumed by shipping, fulfillment, notification
- Settlement → reads from order, payment

### Build Verification
- After making changes, run `mvn install -pl {bc}/{bc}-service -am` — must compile
- After adding DB migrations, run full build: `cd root-build && make build`
- Never commit code that breaks `mvn compile` on any module

## STM State Machine Inventory — 23 Flows

All STM XMLs are in `{bc}/{bc}-service/src/main/resources/com/homebase/ecom/{bc}/{bc}-states.xml` (except rules-engine: `stm/ruleset-lifecycle.xml`).

### Launch-Critical STMs (12 flows)

| Module | Flow ID | States | Events | Auto-States | Key Cross-BC Events |
|--------|---------|--------|--------|-------------|---------------------|
| **user** | user-flow | 8 | 15 | CHECK_VERIFICATION_TIMEOUT | — |
| **product** | product-flow | 8 | 13 | — | supplier.SUSPENDED → disableProduct |
| **inventory** | inventory-flow | 15 | 18 | CHECK_DEPLETION, CHECK_DAMAGE_SEVERITY, CHECK_AFTER_DISCARD | order.CANCELLED → releaseReservedStock |
| **offer** | offer-flow | 9 | 9 | CHECK_AUTO_APPROVE, CHECK_EXPIRATION | product.DISABLED → suspend |
| **cart** | cart-flow | 6 | 17 | — | checkout.COMPENSATED → cancelCheckout |
| **promo** | promo-flow | 8 | 8 | CHECK_USAGE, CHECK_EXPIRATION | — |
| **checkout** | checkout-flow | 8 | 8 | CHECK_RETRY_ALLOWED | payment→paymentSuccess/paymentFailed; publishes cart.completeCheckout, order.create |
| **order** | order-flow | 18 | 18 | CHECK_CANCELLATION_WINDOW, CHECK_FRAUD | payment→paymentSucceeded/Failed; shipping→markDelivered/deliveryFailed; publishes fulfillment.initiate, settlement.calculate |
| **payment** | payment-flow | 17 | 17 | CHECK_RETRY, CHECK_CHARGEBACK_OUTCOME | checkout→process; publishes order.paymentSucceeded/Failed, settlement.calculate |
| **shipping** | shipping-flow | 13 | 13 | CHECK_DELIVERY_ATTEMPTS | fulfillment→createLabel; publishes order.markDelivered/deliveryFailed |
| **notification** | notification-flow | 9 | 8 | CHECK_RETRY | consumed by all BCs via Kafka |
| **settlement** | settlement-flow | 10 | 8 | CHECK_AUTO_APPROVE | order.COMPLETED→calculate; publishes notification, reconciliation |

### Post-Launch STMs (8 flows)

| Module | Flow ID | States | Events | Auto-States |
|--------|---------|--------|--------|-------------|
| **returnrequest** | return-request-flow | 11 | 9 | CHECK_AUTO_APPROVE |
| **review** | review-flow | 8 | 10 | CHECK_AUTO_PUBLISH |
| **support** | support-flow | 9 | 10 | CHECK_SLA, CHECK_AUTO_CLOSE |
| **supplier** | supplier-flow | 8 | 9 | CHECK_PERFORMANCE |
| **onboarding** | onboarding-flow | 8 | 9 | CHECK_TRAINING_COMPLETE, CHECK_TIMEOUT |
| **rules-engine** | ruleSet-flow | 4 | 6 | — |
| **reconciliation** | reconciliation-flow | 9 | 7 | CHECK_MISMATCHES |
| **fulfillment** | fulfillment-flow (saga) | 9 | 9 | — |

### Saga Orchestrators (3 flows, 2 in supplier-lifecycle)

| Module | Flow ID | Steps | Compensation? |
|--------|---------|-------|---------------|
| **fulfillment** | fulfillment-flow | INITIATED→INVENTORY_RESERVED→SHIPMENT_CREATED→SHIPPED→CUSTOMER_NOTIFIED→COMPLETED | Yes (CANCELLING→COMPENSATION_DONE) |
| **return-processing** | return-processing-flow | INITIATED→PICKUP_SCHEDULED→ITEM_RECEIVED→CHECK_ITEM_CONDITION→INVENTORY_RESTOCKED→SETTLEMENT_ADJUSTED→REFUNDED→COMPLETED | Yes (CANCELLING→COMPENSATION_DONE) |
| **supplier-lifecycle** | suspend-flow | INITIATED→PRODUCTS_DISABLED→CATALOG_CLEARED→INVENTORY_FROZEN→ORDERS_CANCELLED→COMPLETED | Yes |
| **supplier-lifecycle** | reactivate-flow | INITIATED→PRODUCTS_ENABLED→CATALOG_RESTORED→INVENTORY_UNFROZEN→COMPLETED | Yes |

### Cross-BC Event Flow Map

```
checkout.COMPLETED ──→ cart.completeCheckout + order.create
checkout.COMPENSATED ─→ cart.cancelCheckout (restore cart to ACTIVE)
payment.SUCCEEDED ────→ order.paymentSucceeded
payment.FAILED ───────→ order.paymentFailed
payment.SETTLED ──────→ settlement.calculate
payment.REFUNDED ─────→ order.completeRefund
order.PAID ───────────→ fulfillment saga.initiate
order.CANCELLED ──────→ inventory.releaseReservedStock + payment.initiateRefund
order.COMPLETED ──────→ settlement.calculate
shipping.DELIVERED ───→ order.markDelivered
shipping.RETURNED ────→ order.deliveryFailed
shipping.LOST ────────→ order.deliveryFailed
returnrequest.APPROVED → return-processing saga.initiate
return-processing.COMPLETED → returnrequest.completeReturn
supplier.SUSPENDED ───→ supplier-lifecycle suspend-flow.initiate
supplier.REACTIVATED ─→ supplier-lifecycle reactivate-flow.initiate
```

### Stateless OWIZ Pipelines (NOT STMs — no state persistence)

| Module | Flow File | Purpose |
|--------|-----------|---------|
| **pricing** | pricing-flow.xml | Stateless price calculation chain |
| **tax** | tax-flow.xml | Stateless GST/tax computation chain |
| **reconciliation** | reconciliation-flow.xml | Internal OWIZ processing within reconciliation batch |

## OWIZ Chain Inventory — 13 Chains, 99 Steps

All OWIZ chains use Chenile's `org.chenile.owiz.impl.Chain` with `<command>` steps attached by index. Located in `{bc}/{bc}-service/src/main/resources/com/homebase/ecom/{bc}/`.

### Stateless Processing Pipelines (3 chains)

| Module | File | Steps | Trigger |
|--------|------|-------|---------|
| **pricing** | `pricing-flow.xml` | 10: fetchBase→applySellerOverride→validateSellerMinPrice→applySegmentPricing→applyPromo→applyBundleDiscount→calculateTCS→applyTax→roundAndFormat→buildPriceBreakdown | Cart/checkout price calculation |
| **tax** | `tax-flow.xml` | 8: classifyHSN→resolveSellerBuyerState→determineGSTType→checkExemptions→applyReverseCharge→computeGSTComponents→calculateTCS→buildTaxBreakdown | Payment/checkout tax computation |
| **reconciliation** | `reconciliation-flow.xml` | 8: fetchGatewayTransactions→fetchSystemTransactions→normalizeFormats→matchTransactions→detectDuplicates→applyToleranceThreshold→classifyMismatches→buildReconciliationReport | Daily batch reconciliation |

### STM Transition Sub-Chains (4 chains)

| Module | File | Chain | Steps | Called By |
|--------|------|-------|-------|-----------|
| **checkout** | `checkout-saga.xml` | checkoutSagaChain | 10: validateAddress→reserveInventory→lockPrices→applyCoupon→calculateTax→estimateDelivery→screenFraud→processPayment→createOrder→confirmCheckout | checkout STM PROCESSING transition |
| **payment** | `payment-processing-chain.xml` | paymentProcessingChain | 7: validatePaymentMethod→selectGateway→tokenizeInstrument→submitToGateway→handle3DSChallenge→verifyGatewayResponse→confirmPayment | payment STM INITIATED→PROCESSING |
| **inventory** | `inventory-inbound-chain.xml` | inboundChain | 6: validateAgainstPO→performQualityCheck→categorizeItems→allocateWarehouseLocation→updateStockLevels→generateGRN | inventory STM STOCK_PENDING→STOCK_INSPECTION |
| **analytics** | `analytics-aggregation-chain.xml` | analyticsChain | 7: fetchDailySales→aggregateByProduct→aggregateByCategory→aggregateBySeller→computeConversionRates→computeCustomerMetrics→buildDailySummary | Scheduler (daily 02:00 AM IST) |

### Multi-Chain Files (3 files, 6 chains)

| Module | File | Chains | Total Steps |
|--------|------|--------|-------------|
| **fulfillment** | `fulfillment-owiz-chains.xml` | 3: pick-chain (6), pack-chain (5), carrier-selection-chain (5) | 16 |
| **return-processing** | `return-processing-owiz-chains.xml` | 3: inspect-item-chain (4), restock-chain (3), refund-chain (3) | 10 |
| **supplier-lifecycle** | `supplier-lifecycle-owiz-chains.xml` | 3: disable-products-chain (3), cancel-orders-chain (3), enable-products-chain (3) | 9 |

### Shipping & Settlement Standalone Chains (2 chains)

| Module | File | Steps | Trigger |
|--------|------|-------|---------|
| **shipping** | `shipping-rate-chain.xml` | 7: fetchAvailableCarriers→calculateZoneRates→applyWeightDimensional→applyFreeShippingRules→applyPeakSurcharge→calculateDeliveryEstimates→rankAndBuildOptions | Checkout delivery estimate |
| **settlement** | `settlement-calculation-chain.xml` | 9: fetchOrderLineItems→calculateCategoryCommission→applyPlatformFees→deductReturnFees→deductChargebacks→calculateGSTOnCommission→applyTDS→computeNetPayout→buildSettlementBreakdown | settlement STM CALCULATING transition |
| **notification** | `notification-dispatch-chain.xml` | 7: resolveTemplate→resolveChannelPreference→checkDNDRegistry→formatMessage→dispatchToChannel→handleBounce→confirmDelivery | notification STM DISPATCHING transition |

## Module Readiness Status

**Launch-Critical (must be production-ready):**

| Module | Status | What's Missing |
|--------|--------|---------------|
| user | READY | - |
| product | READY | - |
| inventory | READY | - |
| offer | READY | - |
| cart | READY | - |
| pricing | PARTIAL | No STM (stateless OWIZ pipeline — OK by design) |
| promo | READY | - |
| checkout | READY | - |
| order | READY | - |
| payment | PARTIAL | No REST controller (event-driven — OK), verify Kafka wiring |
| shipping | READY | - |
| notification | READY | - |

**Post-Launch (can deploy without these):**

| Module | Status | Notes |
|--------|--------|-------|
| fulfillment | READY | DB migrations done |
| return-processing | READY | DB migrations done (saga + inspection/disposition/labels) |
| returnrequest | READY | - |
| settlement | READY | - |
| supplier | READY | - |
| onboarding | READY | - |
| supplier-lifecycle | READY | DB migrations done (saga + performance/probation/appeals) |
| support | READY | - |
| review | READY | - |
| reconciliation | READY | DB migrations done |
| analytics | PARTIAL | Read model only |
| cms | PARTIAL | Read model only |
| catalog | SHELL | Refactored to query-only |
| rules-engine | READY | - |
| cconfig | READY | DB migrations done |
| tax | PARTIAL | Missing STM (stateless OWIZ — OK) |
| warehouse | READY | DB migrations done |

## Project Overview

HomeBase is a Java 25 multi-module Maven ecommerce platform built on the **Chenile framework** (`chenile-parent:2.1.12`). It runs as a **mini-monolith** — all bounded contexts compile into a single deployable JAR (`root-build/build-package`), but each context is structured as if it were an independent microservice.

**IMPORTANT: Read `gemini.md` at project root for the full architecture rules document before making any structural changes.**

## Build & Run Commands

```bash
# Full build (from repo root)
cd root-build && make build            # or: mvn -Drevision=0.0.1-SNAPSHOT install

# Build a single module (e.g., catalog)
mvn install -pl catalog/catalog-service -am

# Run the monolith locally (port 8080)
cd root-build && make run              # or: mvn spring-boot:run -pl root-build/build-package

# Run tests for a single module
mvn test -pl catalog/catalog-service

# Run tests for a single module with integration tests
mvn -DperformIt=true install -pl catalog/catalog-service -am

# Infrastructure (PostgreSQL, Redis, Kafka, Keycloak)
cd docker && docker compose up -d
```

## Test Framework

Tests use **Cucumber BDD** with Chenile's test harness. Each service module has:
- `src/test/resources/features/*.feature` — Gherkin scenarios (REST-driven)
- `src/test/java/.../bdd/CukesRestTest.java` — JUnit runner with `@RunWith(Cucumber.class)`
- `src/test/java/.../bdd/CukesSteps.java` — step definitions
- `src/test/java/.../SpringTestConfig.java` — `@SpringBootApplication` test config with `@ActiveProfiles("unittest")`

Cucumber glue packages always include `org.chenile.cucumber.rest` and `org.chenile.cucumber.workflow` for Chenile's built-in step definitions.

**Available Cucumber steps from Chenile** (from `RestCukesSteps`):
- `When I POST a REST request to URL "{url}" with payload` — POST with JSON body
- `When I GET a REST request to URL "{url}"` — GET request
- `When I PUT a REST request to URL "{url}" with payload` — PUT with JSON body
- `When I PATCH a REST request to URL "{url}" with payload` — PATCH with JSON body
- `When I DELETE a REST request to URL "{url}" with payload` — DELETE request
- `When I construct a REST request with header "{name}" and value "{value}"` — set header
- `Then the http status code is {code}` — assert HTTP status
- `Then the REST response key "{jsonPath}" is "{value}"` — assert response field
- `Then the REST response contains key "{jsonPath}"` — assert key exists
- `Then the REST response does not contain key "{jsonPath}"` — assert key missing
- `And store "{jsonPath}" from response to "{varName}"` — store value for later use (supports `${varName}` substitution in URLs/payloads)
- `Then success is true` / `Then success is false` — assert GenericResponse.success
- `Then the top level code is {code}` — assert GenericResponse error code
- `Then a REST warning must be thrown with code {code}` — assert warning

**Workflow test steps** (from `CucumberWorkflowSteps`):
- `Given that enablement strategy is "{strategyName}"` — set STM enablement
- `Given that a new mandatory activity is added to state "{state}" in flow "{flow}"` — add activity
- `Given that state "{state}" is enabled/disabled in flow "{flow}"` — toggle states

**Security test steps** (from `RestCukesSecSteps`):
- `Given I construct an authorized REST request in realm "{realm}" for user "{user}" and password "{password}"` — authenticate via Keycloak

## Chenile Framework Reference

Chenile (v2.1.12) is the foundational framework. Understanding it is critical for all development.

### Core Architecture: The Interceptor Chain

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

### STM (State Transition Machine) — The Heart of Chenile

STM manages entity lifecycles. Every stateful entity (Order, User, Payment, etc.) has an STM that defines valid states, transitions, and the actions triggered during transitions.

#### STM Class Hierarchy

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

#### STM Core Interfaces

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

#### STM Action Types

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

#### STM Bean Resolution (STMTransitionActionResolver)

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

#### STM XML Configuration Format

State machines are defined in XML files (e.g., `order-states.xml`):

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

#### STM Wiring in Configuration

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

Chenile provides pub/sub abstraction via `chenile-kafka` and `chenile-pub-sub`:

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

`ContextContainer` is a thread-local singleton providing request-scoped data:

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

`IDGenerator` generates unique IDs for entities:
- Pluggable strategies via `IDGenerator.registerStrategy(name, strategy)`
- Default strategy generates UUID-based IDs
- Strategy selection via `ContextContainer` headers
- `BaseJpaEntity.initializeIfRequired()` auto-generates ID on persist

### Proxy / Service Registry

Chenile supports transparent local/remote service invocation:
- `ProxyBuilder` — creates proxies for service interfaces
- `LocalProxyInvoker` — calls service in-process
- `RemoteProxyInvoker` → `HttpInvoker` — calls service over HTTP
- `ProxyTypeRouter` — routes to local or remote based on service registry
- `ServiceRegistryCache` — caches remote service definitions

### Tenancy & Multi-Tenant Support

- `TenantSpecificResourceLoader` — loads resources with tenant-specific overrides
- `TenantRouter` — routes commands based on tenant from headers
- `BaseJpaEntity.tenant` field on all JPA entities
- `ContextContainer.getTenant()` for current tenant
- Query datasources configured per tenant in `query.datasource's`

### Chenile Base Exceptions

All exceptions extend `ErrorNumException`:
- `BadRequestException` — 400 errors (validation failures)
- `NotFoundException` — 404 errors
- `ServerException` — 500 errors
- `ConfigurationException` — misconfiguration errors
- Each carries `errorNum`, `subErrorNum`, `params[]`, and `ResponseMessage`

### ConfigProvider / EnablementStrategy

- `ConfigProvider` — provides key-value configuration, used for feature flags
- `ConfigBasedEnablementStrategy` — enables/disables STM states and transitions dynamically based on ConfigProvider values
- `ConfigProviderImpl` — default implementation

## Architecture

### Module Layout per Bounded Context

Each bounded context (`cart`, `catalog`, `order`, `user`, etc.) follows hexagonal architecture:

| Sub-module | Purpose |
|---|---|
| `{bc}-api` | DTOs, interfaces, enums, exceptions — public contracts |
| `{bc}-domain` | Aggregates, entities, value objects, repository port interfaces (in `port/`) — NO JPA annotations |
| `{bc}-infrastructure` | JPA entities, mappers (domain↔JPA), repository adapters, external service adapters |
| `{bc}-service` | `{bc}Configuration.java` (all `@Bean` wiring), STM actions, controllers, service impls |
| `{bc}-query` | CQRS read side — MyBatis-based query layer |
| `{bc}-client` | Feign/REST client published for other BCs to consume (depends only on `{bc}-api`) |
| `{bc}-scheduler` | Batch/cron jobs (Spring Batch) |
| `{bc}-app` | Spring Boot launcher (when deployed independently) |

### Critical Conventions

**No annotation-based component scanning for beans.** All beans (`@Bean`) are declared explicitly in `{bc}Configuration.java` inside `{bc}-service`. Never use `@Component`, `@Service`, or `@Repository` on domain/infra/service classes.

**Domain purity.** Domain classes must not import JPA, Spring, or infrastructure classes. JPA entities live in `{bc}-infrastructure/persistence/entity/` and are mapped via dedicated mapper classes in `{bc}-infrastructure/persistence/mapper/`.

**STM (State Transition Machine).** Chenile's workflow engine manages entity lifecycles. STM actions follow the bean naming convention `{prefix}{EventId}Action`. Post-save hooks follow `{prefix}{STATE}PostSaveHook`.

**Cross-BC communication.** BCs call each other via client modules. Import `{other-bc}-client` — never import another BC's service or domain directly.

### Shared & Cross-Cutting Modules

- `shared/shared-api` — common DTOs and interfaces used across BCs
- `shared/shared-security` — Keycloak JWT + CORS auto-configuration
- `filter-core/currency-interceptor` — request-scoped currency resolution via `ContextContainer`; domain accesses currency through a `CurrencyResolver` port, never directly
- `core` — shared core utilities
- `root-build/build-package` — the deployable monolith JAR aggregating all service modules
- `root-build/build-configurations` — Spring configuration for the monolith
- `db-migrations` — Liquibase changelogs for all schemas (organized by BC under `db/changelog/{bc}/`)

### Infrastructure Stack

- **Database:** PostgreSQL 16 (`ecommerce_db`)
- **Cache:** Redis 7
- **Auth:** Keycloak 24 (realm: `homebase`, port 8180 in docker-compose)
- **Messaging:** Kafka (Confluent 7.6, KRaft mode)
- **DB Migrations:** Liquibase (changelogs in `db-migrations/`)
- **Deployment:** Docker → Amazon ECS (GitHub Actions CI/CD in `.github/workflows/deploy-aws.yml`)

## Database Schema — 154 Tables, 243 Changesets

All migrations centralized in `db-migrations/src/main/resources/db/changelog/`. Never add per-module migrations.

### Entity Base Classes (Column Standards)

**BaseJpaEntity** (non-STM entities): `id VARCHAR(255) PK`, `created_time`, `last_modified_time`, `last_modified_by VARCHAR(100)`, `tenant VARCHAR(50)`, `created_by VARCHAR(100)`, `version BIGINT`

**AbstractJpaStateEntity** (STM entities — extends BaseJpaEntity): all above + `state_entry_time`, `sla_yellow_date`, `sla_red_date`, `sla_tending_late INT`, `sla_late INT`, `flow_id VARCHAR(100)`, `state_id VARCHAR(100)`

**Every new entity table MUST include the full BaseJpaEntity columns.** Append-only log/event tables (audit_log, integration_log, payment_attempts, etc.) may omit `version`/`last_modified_*` columns.

### STM Tables (23 — extend AbstractJpaStateEntity)

`user_profiles`, `products`, `cart`, `checkout`, `orders`, `payment`, `offer`, `shipments`, `inventory_item`, `supplier`, `supplier_onboarding`, `returnrequest`, `coupon`, `support_tickets`, `settlements`, `notifications`, `fulfillment_orders`, `reconciliation_batches`, `product_reviews`, `policy`, `fulfillment_saga`, `return_processing_saga`, `supplier_lifecycle_saga`

### Module → Tables Mapping

| Module | Tables | STM? |
|--------|--------|------|
| **infrastructure** | batch_job_instance, batch_job_execution, batch_job_execution_params, batch_step_execution, batch_step_execution_context, batch_job_execution_context, service_definition, operation_definition, param_definition, client_service_interceptors, client_op_interceptors | No |
| **user** | user_profiles, user_address, user_activity_log, user_recently_viewed, wishlist_items | Yes |
| **product** | products, product_variants, variant_attributes, product_attribute_values, product_media, variant_media, product_tags, product_activity_log, brands, product_specifications, product_questions, categories, category_attributes, attribute_definitions, attribute_options, media_assets | Yes |
| **inventory** | inventory_item, inventory_item_activity, inventory_reservations, inventory_movements, inventory_damage_records, fulfillment_centers | Yes |
| **order** | orders, order_items, order_activity, order_addresses, order_status_history | Yes |
| **cart** | cart, cart_item, cart_activity_log | Yes |
| **checkout** | checkout, checkout_item, checkout_saga_log | Yes |
| **offer** | offer, offer_activity_log | Yes |
| **payment** | payment, payment_activity_log, payment_refunds, payment_webhook_log, saved_payment_methods | Yes |
| **shipping** | shipments, shipping_activity, shipping_tracking_events, shipping_rates | Yes |
| **notification** | notifications, notification_metadata, notification_activity, notification_preferences, notification_templates, notification_batches | Yes |
| **returnrequest** | returnrequest, returnrequest_activity | Yes |
| **review** | product_reviews, review_activity, review_images, review_votes, review_responses | Yes |
| **settlement** | settlements, settlement_adjustments, settlement_activity, settlement_line_items | Yes |
| **support** | support_tickets, ticket_messages, ticket_activity | Yes |
| **promo** | coupon, coupon_usage_log, promo_code_activity_log, coupon_compatibility | Yes |
| **pricing** | pricing_rules, price_history | No (stateless OWIZ) |
| **tax** | tax_rates, tax_category_mapping, order_tax_lines | No (stateless OWIZ) |
| **supplier** | supplier, supplier_activity | Yes |
| **onboarding** | supplier_onboarding, onboarding_activity | Yes |
| **catalog** | catalog_items, catalog_item_tags, catalog_categories, category_product_mapping, collections, collection_product_mapping | No (query-only) |
| **cms** | cms_pages, banners | No (query-only) |
| **analytics** | daily_sales_summary, product_performance | No (read model) |
| **rules-engine** | policy, policy_rule, policy_rule_metadata, policy_activity_log, decisions, decision_metadata, fact_definition | Yes |
| **cconfig** | cconfig | No |
| **fulfillment** | fulfillment_orders, fulfillment_line_items, pick_lists, pack_slips, delivery_attempts, fulfillment_order_activity | Yes |
| **warehouse** | warehouses, warehouse_locations, warehouse_inventory, warehouse_staff, warehouse_staff_shifts | No |
| **reconciliation** | reconciliation_batches, reconciliation_activity, gateway_transactions, system_transactions, transaction_matches, transaction_mismatches | Yes |
| **saga** | fulfillment_saga, fulfillment_saga_activity, return_processing_saga, return_processing_saga_activity, supplier_lifecycle_saga, supplier_lifecycle_saga_activity, return_inspections, return_dispositions, return_labels, supplier_performance_metrics, supplier_probation_history, supplier_appeal_requests | Yes |
| **cross-cutting** | audit_log, price_lock_records, payment_attempts, integration_log, customer_segments, customer_segment_members, gift_cards, store_credits, commission_rules, order_shipment_groups, risk_signals, gst_reconciliation_periods, supplier_kyc_documents | No |

### Schema Hardening (Applied After All Domain Tables)

- **13 foreign keys** on child tables (cart_activity→cart, user_address→user_profiles, etc.)
- **3 unique constraints** (product_variants.sku, supplier.tax_id, inventory_item.sku)
- **12 composite indexes** for high-volume queries
- **Money fields** fixed from BIGINT to DECIMAL(19,2) on cart/checkout tables
- **Soft delete** (`deleted_at`) on 8 master entity tables
- **Tenant NOT NULL CHECK** constraints on ~80 tables
- All in `infrastructure/db.changelog-schema-hardening.xml` and `infrastructure/db.changelog-missing-tables.xml`

## BDD Test Differentiation

### Service BDD Tests (STM + REST — `{bc}-service`)
These test the **write side** — STM transitions, business rules, controller endpoints:
```bash
mvn test -pl {bc}/{bc}-service          # e.g., mvn test -pl order/order-service
```
- Feature files: `{bc}-service/src/test/resources/features/{bc}.feature`
- Tests: create entity → trigger events → assert state transitions → assert HTTP responses
- Uses Chenile's `RestCukesSteps` for HTTP and `CucumberWorkflowSteps` for STM

### Query BDD Tests (CQRS Read Side — `{bc}-query`)
These test the **read side** — MyBatis queries, search filters, pagination:
```bash
mvn test -pl {bc}/{bc}-query            # e.g., mvn test -pl catalog/catalog-query
```
- Feature files: `{bc}-query/src/test/resources/rest/features/query.feature`
- Tests: search by filters → assert response fields → assert pagination
- Uses Chenile's `RestCukesSteps` only (no STM)
- Test data loaded via Liquibase changelogs in test resources, NEVER via schema.sql

### Scheduler Tests (`{bc}-scheduler`)
Unit tests for batch jobs, not BDD:
```bash
mvn test -pl {bc}/{bc}-scheduler
```

### Test Data
- **All test data via Liquibase** changelogs in `{bc}-query/src/test/resources/liquibase/`
- **Never use** `schema.sql` or `data.sql` — breaks Chenile's test harness
- H2 in `MODE=PostgreSQL` for unit tests, real PostgreSQL for integration tests (`-DperformIt=true`)
