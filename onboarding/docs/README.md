# Onboarding Module

## Purpose
The `onboarding` module handles the process of onboarding new Saree Makers (Suppliers) into the platform. It provides the initial entry point where a supplier's information is submitted, validated, and processed before being officially registered in the system.

## Business Process
When a new Saree Maker registers:
1. A valid onboarding request triggers the `supplierOnboardingWorkflow` (configured via `Easy Flows`).
2. **Validation:** `ValidateOnboardingRequest` checks the details structure.
3. **Record Creation:** `CreateSupplierRecordCommand` calls the `supplier-api` (`SupplierService`) to create a new `Supplier` record via the `STM` state machine, putting it into a `PENDING_REVIEW` state.
4. **Notification:** `NotifyDadCommand` acts as a placeholder or action trigger to notify the Hub Manager for manual review and approval of the newly registered supplier.

## Tech Stack
- **Language**: Java 25
- **Framework**: Spring Boot (3.x)
- **Workflow Orchestration**: `Easy Flows` (`org.jeasy:easy-flows:0.2`). All sequential steps implement the `Work` block.
- **Integration**: Depends on `supplier-api` to interact seamlessly with the existing supplier infrastructure and `STM` capabilities. No direct database access; delegates database operations to explicit domains like `Supplier`.
- **Validation**: Jakarta Validation API for incoming payload validation
- **Data Transfer Objects**: Boilerplate-free DTOs using standard Java patterns (Getters, setters, manual builder pattern). *Note: Lombok was explicitly removed from this module to rely on standard Java constructs.*

## Key Components
- **API (`onboarding-api`)**: Contains DTOs (`SupplierOnboardingRequest`, `OnboardingResponse`) and public API contracts.
- **Service (`onboarding-service`)**: Contains implementations, the `OnboardingController` entry point, workflow definitions (`OnboardingConfiguration`), and command executions (`ValidateOnboardingRequest`, `CreateSupplierRecordCommand`, `NotifyDadCommand`).

## Architectural Constraints (Amazon-grade Discipline)
- This module owns specifically the Onboarding capability and orchestrates the transition of the new prospect supplier into the `supplier` module ecosystem. 
- It does not access the `supplier` module's databases directly.
- Communicates purely with other domains (`supplier`) through API interfaces.
