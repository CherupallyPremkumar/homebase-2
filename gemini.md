# Homebase Platform — Architecture Rules for AI Agents

> Reference implementation: `policy` module (policy-api, policy-domain, policy-infrastructure, policy-service, policy-web, policy-app)

---

## 1. Standard Module Structure

Every bounded context (cart, user, policy, order, etc.) follows this layout:

```
{bc}/
├── {bc}-api/           ← Contracts: DTOs, interfaces, enums, exceptions
├── {bc}-domain/        ← DDD core: aggregate, entities, value objects, ports, events
├── {bc}-infrastructure/← JPA adapters, mappers, Flyway SQL, external API adapters
├── {bc}-service/       ← Orchestration: STM config, actions, service impls, controller
├── {bc}-query-api/     ← (optional) CQRS read DTOs
├── {bc}-query/         ← (optional) MyBatis read layer
├── {bc}-client/        ← (optional) Feign/internal client for cross-BC calls
├── {bc}-web/           ← Angular frontend (if present — see policy-web)
├── {bc}-app/           ← Spring Boot launcher (main class + application.yml + Dockerfile)
└── {bc}-k8s/           ← Kubernetes manifests (Kustomize: base + overlays per env)
```

---

## 2. Chenile Base Class Rules — Which Layer Uses What

### Domain model (`{bc}-domain`)
```java
// Domain aggregate / entity: NO JPA annotations — pure Java
public class Policy extends AbstractExtendedStateEntity   // chenile-corefork/utils
        implements ActivityEnabledStateEntity,            // workflow-api
                   ContainsTransientMap {                 // workflow-api

    @Transient (none here — domain has no JPA)
    private TransientMap transientMap = new TransientMap();
    private List<PolicyActivityLog> activities = ...     // domain activity log (NO @Entity)
}
```

**Rules:**
- ✅ `AbstractExtendedStateEntity` — non-JPA state entity (`chenile-corefork/utils`)
- ✅ `ActivityEnabledStateEntity` — activity log interface (`workflow-api`)
- ✅ `ContainsTransientMap` + `TransientMap` — per-request scratchpad (`workflow-api`)
- ❌ NO `@Entity`, `@Table`, `@Column`, `@OneToMany` — those belong in infra

---

### JPA entity (`{bc}-infrastructure/persistence/entity`)
```java
// Infrastructure JPA entity — has ALL JPA annotations
@Entity
@Table(name = "policy")
public class PolicyEntity extends AbstractJpaStateEntity   // chenile-corefork/jpa-utils
        implements ActivityEnabledStateEntity,             // workflow-api
                   ContainsTransientMap {                  // workflow-api

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private List<PolicyActivityLogEntity> activities = new ArrayList<>();

    @Transient
    private TransientMap transientMap = new TransientMap();

    @Column(name = "name")
    private String name;
    ...
}
```

**Rules:**
- ✅ `AbstractJpaStateEntity` — JPA-annotated STM entity (`chenile-corefork/jpa-utils`)
- ✅ All `@Entity`, `@Table`, `@Column`, `@OneToMany`, `@Embedded`, etc. annotations live HERE
- ✅ Activity log entity also gets `@Entity` and extends `BaseJpaEntity`
- ❌ NO business logic here — only persistence mapping

---

### Infra mapper (`{bc}-infrastructure/persistence/mapper`)
```java
// Bidirectional mapper: domain model ↔ JPA entity
public class PolicyMapper {
    public Policy toModel(PolicyEntity entity) { ... }
    public PolicyEntity toEntity(Policy model) { ... }
}
```
- No Spring annotations — declared as `@Bean` in `{bc}Configuration`

---

### Infra repository adapter (`{bc}-infrastructure/persistence/adapter`)
```java
// Implements the domain port, delegates to Spring Data JPA
public class PolicyRepositoryImpl implements PolicyRepository {
    private final PolicyJpaRepository policyJpaRepository;  // Spring Data
    private final PolicyMapper policyMapper;

    @Override
    public Optional<Policy> findById(String id) {
        return policyJpaRepository.findById(id).map(policyMapper::toModel);
    }
}
```
- No `@Repository`/`@Component` — declared as `@Bean` in `{bc}Configuration`

---

### EntityStore (`{bc}-infrastructure/persistence`)
```java
// Chenile workflow uses EntityStore<T> to load/save the aggregate
public class ChenilePolicyEntityStore extends ChenileEntityStore<Policy> {
    private final PolicyRepositoryImpl repository;
    private final PolicyMapper mapper;
    ...
}
```
- This bridges Chenile's STM with the domain aggregate

---

## 3. Configuration Rules (Explicit Bean Wiring)

All beans are declared **explicitly** in `{bc}Configuration.java` in `{bc}-service`:

```java
@Configuration
@EnableChenileRepositories
public class PolicyConfiguration {

    // Infrastructure
    @Bean PolicyMapper policyMapper() { return new PolicyMapper(); }
    @Bean PolicyJpaRepository policyJpaRepository(...) { ... }
    @Bean PolicyRepositoryImpl policyRepository(PolicyJpaRepository r, PolicyMapper m) {
        return new PolicyRepositoryImpl(r, m);
    }
    @Bean ChenilePolicyEntityStore policyEntityStore(...) { ... }

    // STM
    @Bean STMFlowStoreImpl stmFlowStore(...) { ... }
    @Bean STM<Policy> stm(...) { ... }
    @Bean STMActionsInfoProvider actionsInfoProvider(...) { ... }
    @Bean EntityStore<Policy> entityStore() { return new ChenilePolicyEntityStore(...); }

    // Services
    @Bean PolicyServiceImpl policyService(...) { ... }

    // Actions (named {prefix}{EventId}Action → auto-wired by STM resolver)
    @Bean STMTransitionAction<Policy> policyActivatePolicyAction() { return new ActivatePolicyAction(); }
    ...

    // Post-save hooks (named {STATE}PostSaveHook → auto-called by DefaultPostSaveHook)
    @Bean ACTIVEPolicyPostSaveHook policyACTIVEPostSaveHook() { ... }
}
```

**Rules:**
- ❌ No `@Component`, `@Service`, `@Repository` anywhere in domain, infra, or service
- ✅ Constructor injection everywhere
- ✅ Bean names use prefix convention: `{prefix}{EventId}Action` for STM actions
- ✅ Post-save hook beans follow: `{STATE}{BC}PostSaveHook`

---

## 4. STM Action Rules

```java
// STM transition action — minimal, delegates to service
public class ActivatePolicyAction extends STMTransitionAction<Policy> {
    @Override
    public void doTransition(Policy policy, Object payload, ...) {
        // 1. Validate the payload
        // 2. Apply domain change to entity
        // 3. Put results in transientMap for post-save hook
        policy.getTransientMap().put("event", "ACTIVATED");
    }
}
```
- Actions use `contaitsTransientMap` to pass data to post-save hooks
- NO direct database calls inside actions
- NO event bus calls inside actions — those go in PostSaveHooks

---

## 5. Domain Port Rules (Hexagonal)

All outbound dependencies (DB, external APIs, context extractors) are **interfaces in the domain**:

```
{bc}-domain/port/
├── {Name}Repository.java      ← persistence port
├── CurrencyResolver.java      ← request-scoped context port (reads from ContextContainer)
└── {ExternalService}Port.java ← any other outbound dependency
```

Infrastructure adapters implement these ports — domain never imports infra classes.

---

## 6. Frontend (`{bc}-web`)

Angular SPA served via Nginx, proxied to `{bc}-app`:
- `{bc}-web/nginx.conf` proxies `/api/{bc}/**` → `http://{bc}-app:8080`
- Built with `ng build --configuration production`
- Served on port `80` (Nginx), backend on port `8080`

---

## 7. App Module Rules

```
{bc}-app/
├── src/main/java/{pkg}/{BC}Application.java   ← @SpringBootApplication
├── src/main/resources/application.yml          ← port, datasource, JPA, Flyway
└── Dockerfile                                  ← FROM openjdk:17-jdk-slim
```

`pom.xml` depends on: `{bc}-service`, `{bc}-infrastructure`, `spring-boot-starter-web`, `spring-boot-starter-actuator`, `postgresql` driver (runtime)

---

## 8. Quick Checklist for New Bounded Context

| What | Where |
|---|---|
| DTO, interface, enum, exception | `{bc}-api` |
| Aggregate, entity, value object | `{bc}-domain` |
| Repository **interface** | `{bc}-domain/port` |
| Domain event (Java record) | `{bc}-domain/event` |
| `@Entity` JPA class | `{bc}-infrastructure/persistence/entity` |
| Domain ↔ JPA mapper | `{bc}-infrastructure/persistence/mapper` |
| Repository **implementation** | `{bc}-infrastructure/persistence/adapter` |
| Flyway SQL | `{bc}-infrastructure/resources/db/migration` |
| STM XML | `{bc}-service/resources/{pkg}/{bc}-states.xml` |
| STM actions, post-save hooks | `{bc}-service/cmds`, `{bc}-service/postSaveHooks` |
| Service implementations | `{bc}-service/impl` |
| ALL `@Bean` declarations | `{bc}Configuration.java` in `{bc}-service` |
| REST controller | `{bc}-service/configuration/controller` |
| Spring Boot main class | `{bc}-app` |
| Angular frontend | `{bc}-web` |
| Client (Feign) | `{bc}-client` |
| K8s manifests | `{bc}-k8s/base` + `{bc}-k8s/overlays/{env}` |

---

## 9. Client Module Rules (`{bc}-client`)

The client module is **published BY** this BC so that **other BCs can call it**.
It is NOT used by the owning BC itself.

```
{bc}-client/
└── pom.xml   ← depends ONLY on {bc}-api (DTOs + interfaces)
```

### Direction of dependency (critical)

```
Policy Service          Cart Service            Order Service
publishes               publishes               CONSUMES both:
policy-client  ──────────────────────────────►  policy-client (call for access control)
cart-client    ──────────────────────────────►  cart-client   (read cart state)
```

**To call another BC's service**, import their client in your `pom.xml`:
```xml
<!-- In user-service/pom.xml — to call the Policy service for access control -->
<dependency>
    <groupId>com.homebase.ecom.policy</groupId>
    <artifactId>policy-client</artifactId>
</dependency>
```

**Create `{bc}-client` only when other BCs need to call your service.**
Example: create `user-client` only when `order-service` or `payment-service` needs user profile data.

**Rules:**
- ✅ Depends **only** on `{bc}-api` — no domain, no infra, no service
- ✅ Contains Feign client interfaces or Spring `RestTemplate`/`WebClient` wrappers
- ✅ Other BCs import this client in their `pom.xml` to make safe cross-BC REST calls
- ❌ NO business logic — only HTTP transport
- ❌ NO Spring Boot autoconfiguration — declared as `@Bean` in the **calling** BC's configuration
- ❌ The **owning** BC never imports its own client — it has direct access to its own services

---

## 10. Kubernetes Module Rules (`{bc}-k8s`)

Uses **Kustomize** (not Helm) for environment-specific configuration.

```
{bc}-k8s/
├── base/
│   ├── kustomization.yaml        ← lists all base resources
│   ├── namespace.yaml            ← dedicated namespace per BC
│   ├── deployment-app.yaml       ← {bc}-app container (port 8080)
│   ├── deployment-web.yaml       ← {bc}-web Nginx container (port 80)
│   ├── service-app.yaml          ← NodePort 8080 for {bc}-app
│   └── service-web.yaml          ← NodePort 80 for {bc}-web
└── overlays/
    ├── local/   ← 1 replica, local image tags
    ├── dev/     ← 3 replicas, dev image tags
    ├── sit/     ← 3 replicas, sit image tags
    └── prod/    ← N replicas (set in replica-patch.yaml), prod image tags
```

**Overlay rules:**
- Each overlay has: `kustomization.yaml` (extends `../../base`) + `replica-patch.yaml`
- `replica-patch.yaml` — patches both `app` and `web` Deployments replica count
- ✅ Base defines structure; overlays only patch what differs per environment
- ❌ Do NOT duplicate full manifests in overlays — always patch on top of base

**Naming inside manifests:**
| Resource | Name convention |
|---|---|
| Namespace | `{bc}` |
| Deployment (backend) | `app` |
| Deployment (frontend) | `web` |
| Service (backend) | `app-service` |
| Service (frontend) | `web-service` |

**To apply a specific environment:**
```bash
kubectl apply -k {bc}-k8s/overlays/dev
kubectl apply -k {bc}-k8s/overlays/prod
```

---

## 11. Currency Resolution Rules (filter-core / ContextContainer)

The platform uses `filter-core/currency-interceptor` to resolve the request currency **before** any business logic runs.

### How it works end-to-end

```
HTTP Request (header or Accept-Language)
    ↓
CurrencyInterceptor.doPreProcessing()       ← BaseChenileInterceptor
    │
    ├── 1. x-chenile-region-id header       ← highest priority (e.g. "IN" → "INR")
    ├── 2. Accept-Language locale country    ← auto-detect (e.g. "fr-FR" → "EUR")
    └── 3. Default: "INR"                   ← from CurrencyProperties.defaultCurrency
    ↓
ContextContainer.getInstance().put("x-homebase-currency", "INR")
    ↓
Any service reads: CurrencyInterceptor.getCurrencyFromContext()
```

### Hexagonal pattern — domain NEVER imports CurrencyInterceptor directly

```
{bc}-domain/port/CurrencyResolver.java     ← outbound port (interface)
    ↑ implements
{bc}-infrastructure/adapter/CurrencyResolverAdapter.java  ← calls CurrencyInterceptor.getCurrencyFromContext()
    ↑ injected into
{bc}Configuration.java @Bean currencyResolver()           ← wired explicitly
```

**Rules:**
- ✅ Domain defines `CurrencyResolver` as an **interface (outbound port)**
- ✅ Infrastructure adapter implements it, delegates to `CurrencyInterceptor.getCurrencyFromContext()`
- ✅ `Preferences.defaults(CurrencyResolver)` takes the port by parameter — never calls it statically
- ✅ `CurrencyResolverAdapter` declared as `@Bean` in `{bc}Configuration` — no `@Component`
- ❌ Domain MUST NOT import `CurrencyInterceptor` or `ContextContainer` directly
- ❌ DO NOT hardcode `"INR"` in domain — always go through `CurrencyResolver` port

### Currency properties (application.yml override)

```yaml
currency:
  default: INR              # fallback when no region can be resolved
  mapping:
    IN: INR
    US: USD
    GB: GBP
    EU: EUR
    SG: SGD
```

Built-in region → currency mappings live in `CurrencyMapper` (India→INR, US→USD, GB→GBP, AU→AUD, JP→JPY, SG→SGD, BR→BRL, etc.)

### Two-level currency in UserProfile / Preferences

| Level | Source | When used |
|---|---|---|
| **Saved Preference** | `Preferences.currency` (DB) | User explicitly chose their currency |
| **Request Context** | `ContextContainer` via `CurrencyResolver` | First-provisioning default; API responses |

Priority in API responses: **Saved Preference** → falls back to **ContextContainer** → falls back to `INR`

