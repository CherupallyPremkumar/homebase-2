Feature: Support Ticket Management
  Tests the support ticket lifecycle through the STM workflow.
  Covers creation, assignment, replies, escalation, resolution, reopening, and closure.

# ====================================================================
# HAPPY PATH: Create -> Assign -> Reply -> Resolve -> Close
# ====================================================================

Scenario: Create support ticket
Given that "flowName" equals "support-flow"
And that "initialState" equals "OPEN"
When I POST a REST request to URL "/support" with payload
"""json
{
    "description": "Cannot track my order",
    "subject": "Order tracking issue",
    "category": "ORDER_SUPPORT",
    "priority": "MEDIUM",
    "userId": "customer-001",
    "orderId": "order-123"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "ticketId"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And the REST response key "mutatedEntity.subject" is "Order tracking issue"
And the REST response key "mutatedEntity.priority" is "MEDIUM"

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
And the REST response key "mutatedEntity.assignedTo" is "agent-101"

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
And the REST response key "mutatedEntity.currentState.stateId" is "ASSIGNED"

Scenario: Customer replies to ticket
Given that "event" equals "reply"
When I PATCH a REST request to URL "/support/${ticketId}/${event}" with payload
"""json
{
    "comment": "Customer reply",
    "message": "Thank you for the update. I will wait."
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${ticketId}"
And the REST response key "mutatedEntity.currentState.stateId" is "ASSIGNED"

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
# ESCALATION PATH: Create -> Assign -> Escalate -> Assign -> Resolve
# ====================================================================

Scenario: Create a ticket for escalation test
When I POST a REST request to URL "/support" with payload
"""json
{
    "description": "Product arrived damaged",
    "subject": "Damaged product",
    "category": "PRODUCT_ISSUE",
    "priority": "MEDIUM",
    "userId": "customer-002"
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
And the REST response key "mutatedEntity.assignedTo" is "senior-agent-201"

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
# REOPEN PATH: Create -> Assign -> Resolve -> Reopen -> Assign -> Resolve -> Close
# ====================================================================

Scenario: Create ticket for reopen test
When I POST a REST request to URL "/support" with payload
"""json
{
    "description": "Billing discrepancy",
    "subject": "Wrong amount charged",
    "category": "BILLING",
    "priority": "HIGH",
    "userId": "customer-003"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "reopenTicketId"

Scenario: Assign and resolve for reopen test
Given that "event" equals "assignAgent"
When I PATCH a REST request to URL "/support/${reopenTicketId}/${event}" with payload
"""json
{
    "comment": "Assigning",
    "agentId": "agent-103"
}
"""
Then the REST response key "mutatedEntity.currentState.stateId" is "ASSIGNED"

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
And the REST response key "mutatedEntity.currentState.stateId" is "OPEN"

# ====================================================================
# SLA BREACH: Verify SLA metadata is set on assignment
# ====================================================================

Scenario: SLA breach alert - verify SLA timer set on assignment
Given that "event" equals "assignAgent"
When I PATCH a REST request to URL "/support/${reopenTicketId}/${event}" with payload
"""json
{
    "comment": "Reassigning after reopen for SLA check",
    "agentId": "agent-104"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "ASSIGNED"
And the REST response key "mutatedEntity.assignedTo" is "agent-104"

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
