Feature: Support Ticket Lifecycle
  Tests the full support ticket lifecycle through the STM workflow.
  Covers creation, assignment, in-progress, waiting-on-customer, escalation,
  resolution, reopening, and closure with security (Keycloak).

# ====================================================================
# HAPPY PATH: Create -> Assign -> StartWork -> Reply -> Resolve -> Close
# ====================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Create support ticket
Given that "flowName" equals "support-flow"
And that "initialState" equals "OPEN"
When I POST a REST request to URL "/support" with payload
"""json
{
    "description": "Cannot track my order",
    "subject": "Order tracking issue",
    "category": "ORDER",
    "priority": "MEDIUM",
    "customerId": "customer-001",
    "orderId": "order-123"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "ticketId"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.subject" is "Order tracking issue"
And the REST response key "mutatedEntity.priority" is "MEDIUM"
And the REST response key "mutatedEntity.category" is "ORDER"
And the REST response key "mutatedEntity.customerId" is "customer-001"

Scenario: Assign agent to ticket
Given that "event" equals "assignAgent"
When I PATCH a REST request to URL "/support/${ticketId}/${event}" with payload
"""json
{
    "comment": "Assigning to support agent",
    "agentId": "agent-101"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${ticketId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ASSIGNED"
And the REST response key "mutatedEntity.assignedAgentId" is "agent-101"

Scenario: Agent starts working on ticket
Given that "event" equals "startWork"
When I PATCH a REST request to URL "/support/${ticketId}/${event}" with payload
"""json
{
    "comment": "Starting work"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${ticketId}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_PROGRESS"

Scenario: Agent replies to ticket
Given that "event" equals "reply"
When I PATCH a REST request to URL "/support/${ticketId}/${event}" with payload
"""json
{
    "comment": "Agent reply",
    "message": "I can see your order is currently in transit. It should arrive by tomorrow."
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${ticketId}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_PROGRESS"

Scenario: Agent waits on customer
Given that "event" equals "waitOnCustomer"
When I PATCH a REST request to URL "/support/${ticketId}/${event}" with payload
"""json
{
    "comment": "Need customer confirmation",
    "waitReason": "Awaiting delivery confirmation from customer"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "WAITING_ON_CUSTOMER"

Scenario: Customer responds and work resumes
Given that "event" equals "resumeWork"
When I PATCH a REST request to URL "/support/${ticketId}/${event}" with payload
"""json
{
    "comment": "Customer replied",
    "message": "Yes, I received the package. Thank you!"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_PROGRESS"

Scenario: Resolve ticket
Given that "event" equals "resolve"
When I PATCH a REST request to URL "/support/${ticketId}/${event}" with payload
"""json
{
    "comment": "Resolving ticket",
    "resolution": "Customer confirmed order was delivered successfully."
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${ticketId}"
And the REST response key "mutatedEntity.currentState.stateId" is "RESOLVED"

Scenario: Close ticket after resolution
Given that "event" equals "close"
When I PATCH a REST request to URL "/support/${ticketId}/${event}" with payload
"""json
{
    "comment": "Closing ticket"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${ticketId}"
And the REST response key "mutatedEntity.currentState.stateId" is "CLOSED"

# ====================================================================
# ESCALATION PATH: Create -> Assign -> StartWork -> Escalate -> Assign -> Resolve
# ====================================================================

Scenario: Create a ticket for escalation test
When I POST a REST request to URL "/support" with payload
"""json
{
    "description": "Product arrived damaged",
    "subject": "Damaged product",
    "category": "PRODUCT",
    "priority": "MEDIUM",
    "customerId": "customer-002"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "escalateTicketId"
And the REST response key "mutatedEntity.currentState.stateId" is "OPEN"

Scenario: Assign agent for escalation test
Given that "event" equals "assignAgent"
When I PATCH a REST request to URL "/support/${escalateTicketId}/${event}" with payload
"""json
{
    "comment": "Initial assignment",
    "agentId": "agent-102"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ASSIGNED"

Scenario: Start work then escalate ticket
Given that "event" equals "startWork"
When I PATCH a REST request to URL "/support/${escalateTicketId}/${event}" with payload
"""json
{
    "comment": "Starting work"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "IN_PROGRESS"

Scenario: Escalate ticket to senior agent
Given that "event" equals "escalate"
When I PATCH a REST request to URL "/support/${escalateTicketId}/${event}" with payload
"""json
{
    "comment": "Escalating to senior",
    "escalationReason": "Customer demanding full refund, needs manager approval",
    "escalateTo": "senior-agent-201"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${escalateTicketId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ESCALATED"
And the REST response key "mutatedEntity.priority" is "HIGH"
And the REST response key "mutatedEntity.assignedAgentId" is "senior-agent-201"

Scenario: Reassign escalated ticket
Given that "event" equals "assignAgent"
When I PATCH a REST request to URL "/support/${escalateTicketId}/${event}" with payload
"""json
{
    "comment": "Reassigning after escalation",
    "agentId": "senior-agent-201"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ASSIGNED"

Scenario: Resolve the escalated ticket
Given that "event" equals "resolve"
When I PATCH a REST request to URL "/support/${escalateTicketId}/${event}" with payload
"""json
{
    "comment": "Resolved by senior agent",
    "resolution": "Full refund issued for damaged product."
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "RESOLVED"

# ====================================================================
# REOPEN PATH: Create -> Assign -> StartWork -> Resolve -> Reopen -> Assign -> Resolve -> Close
# ====================================================================

Scenario: Create ticket for reopen test
When I POST a REST request to URL "/support" with payload
"""json
{
    "description": "Billing discrepancy",
    "subject": "Wrong amount charged",
    "category": "PAYMENT",
    "priority": "HIGH",
    "customerId": "customer-003"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "reopenTicketId"

Scenario: Assign and start work for reopen test
Given that "event" equals "assignAgent"
When I PATCH a REST request to URL "/support/${reopenTicketId}/${event}" with payload
"""json
{
    "comment": "Assigning",
    "agentId": "agent-103"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ASSIGNED"

Scenario: Start work for reopen test
Given that "event" equals "startWork"
When I PATCH a REST request to URL "/support/${reopenTicketId}/${event}" with payload
"""json
{
    "comment": "Starting"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "IN_PROGRESS"

Scenario: Resolve before reopen
Given that "event" equals "resolve"
When I PATCH a REST request to URL "/support/${reopenTicketId}/${event}" with payload
"""json
{
    "comment": "Resolving",
    "resolution": "Billing corrected."
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "RESOLVED"

Scenario: Reopen resolved ticket
Given that "event" equals "reopen"
When I PATCH a REST request to URL "/support/${reopenTicketId}/${event}" with payload
"""json
{
    "comment": "Reopening",
    "reason": "The billing issue recurred on the next statement."
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${reopenTicketId}"
And the REST response key "mutatedEntity.currentState.stateId" is "REOPENED"
And the REST response key "mutatedEntity.reopenCount" is "1"

Scenario: Reassign reopened ticket
Given that "event" equals "assignAgent"
When I PATCH a REST request to URL "/support/${reopenTicketId}/${event}" with payload
"""json
{
    "comment": "Reassigning after reopen",
    "agentId": "agent-104"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "ASSIGNED"
And the REST response key "mutatedEntity.assignedAgentId" is "agent-104"

# ====================================================================
# INVALID: Send invalid event to closed ticket
# ====================================================================

Scenario: Send an invalid event to a closed support ticket
When I PATCH a REST request to URL "/support/${ticketId}/invalidEvent" with payload
"""json
{
    "comment": "Invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
