Feature: Tests the full Shipping Lifecycle using Chenile STM.
Covers the happy path: AWAITING_PICKUP -> PICKED_UP -> IN_TRANSIT -> OUT_FOR_DELIVERY -> DELIVERED,
and the return path: DELIVERED -> RETURN_REQUESTED -> returnPickup -> IN_TRANSIT.
Includes activity tracking verification.

Scenario: Create a new shipment in AWAITING_PICKUP state
Given that "flowName" equals "shipping-flow"
And that "initialState" equals "AWAITING_PICKUP"
When I POST a REST request to URL "/shipping" with payload
"""json
{
    "description": "Order shipment for ORD-2024-001",
    "orderId": "ORD-2024-001",
    "shippingAddress": "123 Main St, Bangalore"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "Order shipment for ORD-2024-001"

Scenario: Retrieve the shipment that was just created
When I GET a REST request to URL "/shipping/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "AWAITING_PICKUP"

Scenario: Assign courier and pick up (AWAITING_PICKUP -> PICKED_UP)
Given that "comment" equals "Courier DHL-12345 assigned, package picked up from warehouse"
And that "event" equals "courierAssigned"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "carrier": "DHL EXPRESS",
    "trackingNumber": "DHL-12345",
    "estimatedDeliveryDays": 4
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "PICKED_UP"
And the REST response key "mutatedEntity.carrier" is "DHL EXPRESS"
And the REST response key "mutatedEntity.trackingNumber" is "DHL-12345"
And the REST response contains key "mutatedEntity.shippedAt"
And the REST response contains key "mutatedEntity.estimatedDelivery"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Mark shipment in transit (PICKED_UP -> IN_TRANSIT)
Given that "comment" equals "Package left sorting facility, in transit to destination"
And that "event" equals "inTransit"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "currentLocation": "Delhi Distribution Center"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_TRANSIT"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Mark shipment out for delivery (IN_TRANSIT -> OUT_FOR_DELIVERY)
Given that "comment" equals "Package arrived at local hub, out for delivery"
And that "event" equals "outForDelivery"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "localHub": "Bangalore Central Hub"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "OUT_FOR_DELIVERY"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Mark shipment as delivered (OUT_FOR_DELIVERY -> DELIVERED)
Given that "comment" equals "Package delivered to customer, signed by recipient"
And that "event" equals "markDelivered"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "deliveryProof": "signature-img-001.png",
    "receivedBy": "Customer"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"
And the REST response contains key "mutatedEntity.deliveredAt"
And the REST response key "mutatedEntity.deliveryProof" is "signature-img-001.png"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Verify delivered shipment via GET
When I GET a REST request to URL "/shipping/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELIVERED"

Scenario: Request return on delivered shipment (DELIVERED -> RETURN_REQUESTED)
Given that "comment" equals "Customer requested return, item damaged on arrival"
And that "event" equals "returnRequested"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "returnReason": "Damaged on arrival"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "RETURN_REQUESTED"
And the REST response key "mutatedEntity.returnReason" is "Damaged on arrival"
And the REST response contains key "mutatedEntity.returnTrackingNumber"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Initiate return pickup (RETURN_REQUESTED -> IN_TRANSIT)
Given that "comment" equals "Return pickup scheduled, courier dispatched"
And that "event" equals "returnPickup"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "returnCarrier": "BLUEDART",
    "returnTrackingNumber": "BD-RTN-12345"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "IN_TRANSIT"
And the REST response key "mutatedEntity.returnTrackingNumber" is "BD-RTN-12345"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Add mandatory activities for activity tracking test
Given that "terminalState" equals "__TERMINAL_STATE__"
And that config strategy is "shippingConfigProvider" with prefix "Shipping"
And that a new mandatory activity "verifyReturnCondition" is added from state "${finalState}" to state "${finalState}" in flow "${flowName}"
And that a new mandatory activity "updateInventory" is added from state "${finalState}" to state "${finalState}" in flow "${flowName}"
And that a new state "${terminalState}" is added to flow "${flowName}"
And that a new activity completion checker "completeReturn" is added from state "${finalState}" to state "${terminalState}" in flow "${flowName}"
And that "comment" equals "Attempting completeReturn without completing mandatory activities"
And that "event" equals "completeReturn"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response does not contain key "mutatedEntity"
And success is false
And the http status code is 400
And the top level subErrorCode is 49000

Scenario: Perform mandatory activity verifyReturnCondition
Given that "comment" equals "Return item condition verified as damaged"
And that "event" equals "verifyReturnCondition"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${finalState}"
And the REST response key "mutatedEntity.activities" collection has an item with keys and values:
| key             | value                                    |
| activityName    | ${event}                                 |
| activityComment | ${comment}                               |

Scenario: Perform mandatory activity updateInventory
Given that "comment" equals "Inventory updated for returned item"
And that "event" equals "updateInventory"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${finalState}"
And the REST response key "mutatedEntity.activities" collection has an item with keys and values:
| key             | value                                    |
| activityName    | ${event}                                 |
| activityComment | ${comment}                               |

Scenario: Complete return after all mandatory activities done
Given that "comment" equals "All return activities completed, transitioning"
And that "event" equals "completeReturn"
When I PATCH a REST request to URL "/shipping/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${terminalState}"

Scenario: Send an invalid event to shipping. This will err out.
When I PATCH a REST request to URL "/shipping/${id}/invalid" with payload
"""json
{
    "comment": "invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422
