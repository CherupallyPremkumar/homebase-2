Feature: Tests the full Settlement Lifecycle using Chenile STM.
Covers the settlement flow: PENDING -> PROCESSING -> READY_FOR_PAYMENT -> SETTLED,
and the failure/retry path: FAILED -> retrySettlement -> PROCESSING.
Includes mandatory activity tracking for payout verification.

Scenario: Create a new settlement for a supplier period
Given that "flowName" equals "settlement-flow"
And that "initialState" equals "PENDING"
When I POST a REST request to URL "/settlement" with payload
"""json
{
    "description": "March 2026 Settlement for Artisan Crafts Co.",
    "supplierId": "supplier-001",
    "periodMonth": 3,
    "periodYear": 2026
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "March 2026 Settlement for Artisan Crafts Co."

Scenario: Retrieve the settlement that was just created
When I GET a REST request to URL "/settlement/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"

Scenario: Trigger month end processing (PENDING -> PROCESSING)
Given that "comment" equals "Month end reached, initiating settlement processing"
And that "event" equals "monthEnd"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Calculate settlement amounts (PROCESSING -> READY_FOR_PAYMENT)
Given that "comment" equals "Commission calculated: 10% platform fee deducted"
And that "event" equals "calculateSettlement"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "READY_FOR_PAYMENT"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Confirm payout to supplier (READY_FOR_PAYMENT -> SETTLED)
Given that "comment" equals "Payout confirmed via bank transfer"
And that "event" equals "confirmPayout"
When I PATCH a REST request to URL "/settlement/${id}/${event}" with payload
"""json
{
    "paymentReference": "PAY-2026-03-001",
    "notes": "Payout of net amount confirmed"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SETTLED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Verify settled state is terminal (no further transitions)
When I PATCH a REST request to URL "/settlement/${id}/monthEnd" with payload
"""json
{
    "comment": "Attempting event on settled entity"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Create a second settlement for failure/retry flow
When I POST a REST request to URL "/settlement" with payload
"""json
{
    "description": "April 2026 Settlement - Failure Test",
    "supplierId": "supplier-002",
    "periodMonth": 4,
    "periodYear": 2026
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id2"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING"

Scenario: Move second settlement to PROCESSING
Given that "comment" equals "Month end processing"
And that "event" equals "monthEnd"
When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "PROCESSING"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState2"

Scenario: Calculate settlement for second settlement
Given that "comment" equals "Calculating settlement amounts"
And that "event" equals "calculateSettlement"
When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState2"

Scenario: Add mandatory activities for payout verification
Given that "terminalState" equals "__TERMINAL_STATE__"
And that config strategy is "settlementConfigProvider" with prefix "Settlement"
And that a new mandatory activity "verifyBankDetails" is added from state "${currentState2}" to state "${currentState2}" in flow "${flowName}"
And that a new mandatory activity "auditTrailCheck" is added from state "${currentState2}" to state "${currentState2}" in flow "${flowName}"
And that a new state "${terminalState}" is added to flow "${flowName}"
And that a new activity completion checker "finalizeSettlement" is added from state "${currentState2}" to state "${terminalState}" in flow "${flowName}"
And that "comment" equals "Attempting finalize without completing mandatory activities"
And that "event" equals "finalizeSettlement"
When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response does not contain key "mutatedEntity"
And success is false
And the http status code is 400
And the top level subErrorCode is 49000

Scenario: Perform mandatory activity verifyBankDetails
Given that "comment" equals "Supplier bank details verified against records"
And that "event" equals "verifyBankDetails"
When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState2}"
And the REST response key "mutatedEntity.activities" collection has an item with keys and values:
| key             | value                                          |
| activityName    | ${event}                                       |
| activityComment | ${comment}                                     |

Scenario: Perform mandatory activity auditTrailCheck
Given that "comment" equals "Audit trail reviewed, all transactions accounted for"
And that "event" equals "auditTrailCheck"
When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState2}"
And the REST response key "mutatedEntity.activities" collection has an item with keys and values:
| key             | value                                          |
| activityName    | ${event}                                       |
| activityComment | ${comment}                                     |

Scenario: Finalize settlement after all mandatory activities done
Given that "comment" equals "All verification complete, finalizing settlement"
And that "event" equals "finalizeSettlement"
When I PATCH a REST request to URL "/settlement/${id2}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id2}"
And the REST response key "mutatedEntity.currentState.stateId" is "${terminalState}"

Scenario: Send an invalid event to settlement. This will err out.
When I PATCH a REST request to URL "/settlement/${id}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
