# CLAUDE.md

This file provides guidance to Claude Code when working with this repository.

**Detailed reference docs** (read on demand, not loaded automatically):
- `docs/claude-reference/chenile-deep-reference.md` — Full Chenile framework code examples, STM wiring, controller patterns, entity hierarchy
- `docs/claude-reference/stm-owiz-inventory.md` — All 23 STM flows, 13 OWIZ chains, cross-BC event map
- `docs/claude-reference/database-schema.md` — 154 tables, module→table mapping, schema hardening, module readiness

## Mandatory Behavior — Read First

**Think like a senior architect and developer on every task.** Before writing or modifying any code:

1. **Understand first.** Read the existing code, understand what's already built, and how it fits the architecture. Never leave dead code behind.
2. **Ask "where does this logic belong?"** Apply SOLID principles, hexagonal architecture, and DDD. Domain logic in domain. Infrastructure in infrastructure. Never mix layers.
3. **Follow Chenile patterns exactly.** All mutations through STM `processById()`, never direct save. All beans via `@Bean` in Configuration — never `@Component`/`@Service`/`@Repository`. Read `docs/claude-reference/chenile-deep-reference.md` before making changes.
4. **One design for ALL modules.** Every bounded context MUST follow the SAME hexagonal architecture blueprint (see `reference_hexagonal_blueprint.md` in memory). No per-module variations. Domain model → `AbstractExtendedStateEntity` (no JPA). JPA entity → `AbstractJpaStateEntity` (infrastructure only). Same `@Bean` wiring order in every Configuration. Same folder structure. Textbook consistency across all 31+ modules.
5. **Never modify Chenile framework code.** Fix issues on the HomeBase side only.
6. **Java 25 only.** Never downgrade to 17 or any earlier version.
7. **Liquibase only.** DB migrations live in each module's `{bc}-liquibase` sub-module (e.g., `cart/cart-liquibase`). No centralized `db-migrations/` module. All test data via Liquibase changelogs, never `schema.sql`.
8. **No stubs in main code.** Stub/mock port beans belong only in test `SpringTestConfig`, never in production `{bc}Configuration`.
9. **No frontend in backend repo.** Never create `-web` modules here.
10. **Check memory** at `~/.claude/projects/-Users-premkumar-HomeBase/memory/` for module plans, design decisions, and feedback before starting work on any module.
11. **Production-grade only.** This system serves 1 lakh+ concurrent users. No shortcuts, no TODO stubs, no "good enough." Every line of code must be production-ready.

## Production Design Standards (Amazon-Level)

### API Design
- Every REST endpoint returns `GenericResponse<T>` — never raw objects
- All mutations are **idempotent** — use idempotency keys for POST operations
- All list endpoints support **pagination** (`page`, `size`, `sort`) — never return unbounded results
- Consistent error responses: `errorNum` + `subErrorNum` + human-readable `description`
- All IDs are UUIDs — never auto-increment integers exposed externally
- API versioning via URL path (`/v1/orders`) for breaking changes

### Resilience & Fault Tolerance
- **Circuit breakers** on ALL cross-service calls (Resilience4j)
- **Retry with exponential backoff** — max 3 retries, 100ms/200ms/400ms
- **Timeouts** on every external call — 5s default, 30s max for batch
- **Bulkhead pattern** for thread pool isolation between services
- **Fallback responses** for non-critical failures
- **Dead Letter Queue (DLQ)** for every Kafka consumer — 3 retries

### Data Integrity
- **Optimistic locking** via `@Version` on all JPA entities (already in BaseJpaEntity)
- **Database constraints** enforce business rules — not just application code
- **Unique indexes** on business keys (SKU, email, order number)
- **Foreign keys** for referential integrity — no orphan records
- **Soft delete** via status field — never hard delete production data
- All timestamps in **UTC** — never local timezone

### Performance (1 Lakh+ Concurrent Users)
- **Database indexes** on every column used in WHERE, JOIN, ORDER BY
- **Connection pooling** via HikariCP — max 30 connections per instance
- **Redis caching** for: product catalog, pricing rules, user sessions, cart state
- **Kafka for async** — never synchronous cross-service calls in the request path
- **N+1 query prevention** — use `@EntityGraph` or `JOIN FETCH`, never lazy load in loops
- **Pagination everywhere** — LIMIT/OFFSET or keyset pagination
- Query timeout: 5s max per DB query

### Testing Standards
- Every STM flow must have **BDD tests for ALL state transitions** — happy path AND error paths
- Every transition action must test: valid transition, invalid state rejection, payload validation
- **No module ships without passing BDD tests** — `mvn test -pl {bc}/{bc}-service` must pass
- Integration tests (`-DperformIt=true`) must pass before production-ready

### Security
- **Authentication required** on all endpoints except health checks
- **Authorization** via STM event ACLs (`meta-acl`) — never skip
- **Input validation** at controller layer — never trust client data
- **SQL injection prevention** — always parameterized queries (MyBatis `#{}`, never `${}`)
- **No secrets in code** — all secrets via environment variables / AWS SSM
- **Audit trail** via `ActivityEnabledStateEntity` on all stateful entities

### Observability
- Health checks at `/actuator/health` — must check DB, Redis, Kafka
- Prometheus metrics at `/actuator/prometheus`
- Structured logging with correlation IDs (Chenile trajectory)

## Multi-Agent Coordination Rules

### MANDATORY: Agent Status on Startup (Non-Negotiable)
Every Claude Code session MUST do this **immediately on startup, before any other work**:
1. **Read** all `project_agent_*_status.md` files in `~/.claude/projects/-Users-premkumar-HomeBase/memory/` to see what other agents are doing.
2. **Write** your own status file: `project_agent_{terminal}_status.md` with:
   - Date and time
   - Which terminal you're in (check with `tty` command)
   - What task the user has asked you to do
   - Which modules/bounded contexts you're touching
3. **Update** your status file whenever you switch tasks or start working on a different module.
4. **Delete** your status file when the user ends the session or says you're done.

Example status file:
```markdown
---
name: Agent status - terminal s007
description: Active agent session status for coordination
type: project
---
**Terminal:** /dev/ttys007
**Started:** 2026-04-04 6:17PM
**Task:** Working on analytics query modules
**Modules:** analytics, reconciliation, reporting
**Status:** Active
```

### Module Ownership During Work
- **One agent per bounded context at a time.** If a status file shows another agent is working on `order`, don't touch `order`.
- **Save a memory** when you START working on a module: `project_{bc}_in_progress.md` with date and what you're doing.
- **Delete the in-progress memory** when you finish.
- **Shared files require extra care:** `root-build/build-package/pom.xml`, root `pom.xml` — always read latest before editing.

### ABSOLUTE Rule: Never Push to Git
- **NEVER** run `git push` under any circumstances unless the user explicitly says "push".
- This applies to all agents, all sessions, all terminals. No exceptions.

### Safe Parallel Work Zones (8 Domain Groups)
- **catalog/** — product, offer, pricing, catalog, search, brand-registry
- **buy-path/** — cart, checkout, promo, wishlist, gift-card, wallet, subscription, deals
- **order-management/** — order, payment, returnrequest, return-processing, settlement, invoice, warranty
- **supply-chain/** — inventory, shipping, fulfillment, warehouse, digital-delivery
- **identity/** — user, supplier, onboarding, seller, seller-lifecycle, supplier-lifecycle, address
- **engagement/** — notification, support, review, disputes, recommendation, messaging, loyalty
- **analytics/** — analytics, reconciliation, reporting, clickstream, advertising, affiliate, seller-analytics
- **platform/** — rules-engine, cconfig, compliance, tax, cms, media, auth, audit, core, shared, kafka, organisation, scheduler, fraud, experimentation, i18n

Cross-group dependencies: Cart→pricing/inventory/product clients. Checkout→cart/pricing/order/payment. Order→publishes to shipping/fulfillment/notification. Settlement→reads from order/payment.

### Build Verification
- After making changes, run `mvn install -pl {bc}/{bc}-service -am` — must compile
- After adding DB migrations, run full build: `cd root-build && make build`
- Never commit code that breaks `mvn compile` on any module

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

Cucumber glue packages always include `org.chenile.cucumber.rest` and `org.chenile.cucumber.workflow`.

**Key Cucumber steps** (from `RestCukesSteps`):
- `When I POST/GET/PUT/PATCH/DELETE a REST request to URL "{url}" [with payload]`
- `When I construct a REST request with header "{name}" and value "{value}"`
- `Then the http status code is {code}` / `Then success is true/false`
- `Then the REST response key "{jsonPath}" is "{value}"`
- `And store "{jsonPath}" from response to "{varName}"` — supports `${varName}` substitution

**Security steps** (from `RestCukesSecSteps`):
- `Given I construct an authorized REST request in realm "{realm}" for user "{user}" and password "{password}"`

**Test data:** All via Liquibase changelogs in `{bc}-query/src/test/resources/liquibase/`, NEVER via `schema.sql`.

## Chenile Quick Reference

> For full code examples and detailed patterns, read `docs/claude-reference/chenile-deep-reference.md`.

### Key Concepts
- **ChenileExchange** — request/response context through the interceptor chain
- **ContextContainer** — thread-local per-request context (tenant, userId, region). Domain code must NEVER access directly — use port interfaces
- **GenericResponse\<T\>** — standard response wrapper with `success`, `data`, `errors[]`
- **STM** — state machine engine managing entity lifecycles via XML-defined flows
- **OWIZ** — orchestration framework for interceptor chains and processing pipelines

### STM Bean Naming Convention (Critical)
```
Transition actions:  {prefix}{EventId}Action           e.g., orderPaymentSucceededAction
Post-save hooks:     {prefix}{StateId}PostSaveHook     e.g., orderCREATEDPostSaveHook
Auto-state actions:  {prefix}{StateId}AutoState         e.g., orderCHECK_FRAUDAutoState
```

### STM Configuration Wiring Order (Every BC Must Follow)
1. `BeanFactoryAdapter` — bridges Spring → Chenile
2. `STMFlowStoreImpl` — loads XML, holds state machine definition
3. `STM<T>` — the state machine engine
4. `STMActionsInfoProvider` — metadata about allowed actions per state
5. `STMTransitionActionResolver` — finds beans by naming convention
6. `GenericEntryAction<T>` — persist entity, invoke post-save hooks
7. `EntityStore<T>` — bridges domain repository with Chenile
8. `StateEntityServiceImpl<T>` — the main service bean (name: `_{bc}StateEntityService_`)
9. Individual transition actions (named by convention)
10. Post-save hooks (named by convention)
11. `StmAuthoritiesBuilder` — security authorities supplier for controller ACL

### Entity Pattern
- **Domain model** (`{bc}-domain`): extends `AbstractExtendedStateEntity` — NO JPA annotations
- **JPA entity** (`{bc}-infrastructure`): extends `AbstractJpaStateEntity` — WITH JPA annotations
- **Mapper** (`{bc}-infrastructure`): converts between domain model and JPA entity
- **EntityStore**: bridges Chenile with JPA persistence, extends `ChenileJpaEntityStore`

### Controller Pattern
- Extends `ControllerSupport`, annotated with `@ChenileController(serviceName=...)`
- `@SecurityConfig(authorities={...})` for static ACL, `@SecurityConfig(authoritiesSupplier="...")` for dynamic ACL from STM
- `@BodyTypeSelector("...")` resolves payload type based on eventId
- All operations delegate to `process(request, ...)` from `ControllerSupport`

### STM XML Format
- `<event-information>` — defines events with `meta-acl` and `meta-bodyType`
- `<flow id="..." initial-state="...">` — named workflow
- `<state id="...">` with `<transition event="..." new-state="..."/>` — state transitions
- `<auto-state id="...">` — decision points using `ifAction`/`switchAction` with OGNL conditions
- `isFinal="true"` — terminal states
- All XMLs at `{bc}/{bc}-service/src/main/resources/com/homebase/ecom/{bc}/{bc}-states.xml`

### Security Model
1. **Spring Security**: Keycloak OAuth2/JWT, multi-tenant JWT decoders per realm
2. **Chenile SecurityInterceptor**: checks `@SecurityConfig` ACLs in interceptor chain
3. **STM Security** (`StmSecurityStrategyImpl`): checks `meta-acl` per event in STM XML

### Pub/Sub (Kafka)
- `ChenilePub.publish(topic, body, headers)` / `asyncPublish(...)` / `publishToOperation(...)`
- `PubSubEntryPoint` routes incoming messages to Chenile service operations
- Messages carry Chenile headers for context propagation

## Architecture

### Module Layout per Bounded Context

| Sub-module | Purpose |
|---|---|
| `{bc}-api` | DTOs, interfaces, enums, exceptions — public contracts |
| `{bc}-domain` | Aggregates, entities, value objects, repository port interfaces — NO JPA |
| `{bc}-infrastructure` | JPA entities, mappers, repository adapters, external service adapters |
| `{bc}-service` | `{bc}Configuration.java` (all `@Bean` wiring), STM actions, controllers |
| `{bc}-query` | CQRS read side — MyBatis-based query layer |
| `{bc}-client` | Feign/REST client for other BCs (depends only on `{bc}-api`) |
| `{bc}-scheduler` | Batch/cron jobs (Spring Batch) |

### Critical Conventions

- **No annotation-based component scanning.** All beans via `@Bean` in `{bc}Configuration.java`. Never `@Component`/`@Service`/`@Repository`.
- **Domain purity.** Domain classes must not import JPA, Spring, or infrastructure classes.
- **STM bean naming.** Actions: `{prefix}{EventId}Action`. Post-save hooks: `{prefix}{STATE}PostSaveHook`.
- **Cross-BC communication.** Import `{other-bc}-client` — never import another BC's service or domain directly.

### Shared & Cross-Cutting Modules

- `shared/shared-api` — common DTOs and interfaces across BCs
- `shared/shared-security` — Keycloak JWT + CORS auto-configuration
- `filter-core/currency-interceptor` — request-scoped currency resolution
- `core` — shared core utilities
- `root-build/build-package` — deployable monolith JAR

### Infrastructure Stack

- **Database:** PostgreSQL 16 (`ecommerce_db`)
- **Cache:** Redis 7
- **Auth:** Keycloak 24 (realm: `homebase`, port 8180 in docker-compose)
- **Messaging:** Kafka (Confluent 7.6, KRaft mode)
- **DB Migrations:** Liquibase (per-module in `{bc}/{bc}-liquibase`)
- **Deployment:** Docker → Amazon ECS (GitHub Actions CI/CD)

### Database Basics

> For full table mapping, see `docs/claude-reference/database-schema.md`.

- **BaseJpaEntity columns:** `id VARCHAR(255) PK`, `created_time`, `last_modified_time`, `last_modified_by`, `tenant`, `created_by`, `version BIGINT`
- **AbstractJpaStateEntity** adds: `state_entry_time`, `sla_yellow_date`, `sla_red_date`, `sla_tending_late`, `sla_late`, `flow_id`, `state_id`
- **Every new table MUST include full BaseJpaEntity columns.** Log tables may omit `version`/`last_modified_*`.
- **23 STM tables** and **154 total tables** across all modules.
