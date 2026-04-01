Feature: User Lifecycle -- tests the full user state machine through all paths.
  States: REGISTERED -> EMAIL_VERIFIED -> ACTIVE -> SUSPENDED -> DEACTIVATED
  KYC: ACTIVE -> KYC_PENDING -> KYC_VERIFIED
  Lock: ACTIVE -> LOCKED -> ACTIVE

# ===========================================================================
# HAPPY PATH: Register -> Verify Email -> Activate -> Active
# ===========================================================================

Background:
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"

Scenario: Register a new user in REGISTERED state
Given that "flowName" equals "user-flow"
And that "initialState" equals "REGISTERED"
When I POST a REST request to URL "/user" with payload
"""json
{
    "description": "New User Registration",
    "firstName": "Prem",
    "lastName": "Kumar",
    "email": "prem@homebase.com",
    "phone": "+91-9876543210"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"

Scenario: Retrieve the registered user
When I GET a REST request to URL "/user/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REGISTERED"

Scenario: Resend verification email (stays REGISTERED)
Given that "comment" equals "Resending verification email"
And that "event" equals "resendVerification"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "REGISTERED"

Scenario: Verify email (REGISTERED -> EMAIL_VERIFIED)
Given that "comment" equals "Email verified via OTP"
And that "event" equals "verifyEmail"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "EMAIL_VERIFIED"

Scenario: Activate user (EMAIL_VERIFIED -> ACTIVE)
Given that "comment" equals "System activation"
And that "event" equals "activate"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ===========================================================================
# PROFILE MANAGEMENT: Update profile, Add address, Change password
# ===========================================================================

Scenario: Update profile (ACTIVE -> ACTIVE)
Given that "comment" equals "User updated their profile"
And that "event" equals "updateProfile"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Add address (ACTIVE -> ACTIVE)
Given that "event" equals "addAddress"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "Adding home address",
    "label": "Home",
    "line1": "123 Main Street",
    "city": "Bangalore",
    "state": "Karnataka",
    "postalCode": "560001",
    "country": "IN"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Change password (ACTIVE -> ACTIVE)
Given that "event" equals "changePassword"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "User changed password",
    "oldPassword": "OldPass123!",
    "newPassword": "NewPass456!"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ===========================================================================
# KYC FLOW: ACTIVE -> KYC_PENDING -> KYC_VERIFIED
# ===========================================================================

Scenario: Submit KYC (ACTIVE -> KYC_PENDING)
Given that "comment" equals "Submitting KYC documents"
And that "event" equals "submitKyc"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "KYC_PENDING"

Scenario: Verify KYC (KYC_PENDING -> KYC_VERIFIED)
Given that "comment" equals "KYC documents verified"
And that "event" equals "verifyKyc"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "KYC_VERIFIED"

# ===========================================================================
# SUSPEND/DEACTIVATE FROM KYC_VERIFIED
# ===========================================================================

Scenario: Suspend KYC-verified user (KYC_VERIFIED -> SUSPENDED)
Given that "comment" equals "Fraud detected"
And that "event" equals "suspend"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SUSPENDED"

Scenario: Reinstate suspended user (SUSPENDED -> ACTIVE)
Given that "comment" equals "Investigation cleared, reinstating"
And that "event" equals "reinstateUser"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ===========================================================================
# LOCK/UNLOCK FLOW: ACTIVE -> LOCKED -> ACTIVE
# ===========================================================================

Scenario: Lock account after failed login attempts (ACTIVE -> LOCKED)
Given that "comment" equals "5 consecutive failed login attempts detected"
And that "event" equals "lockAccount"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "LOCKED"

Scenario: Unlock account (LOCKED -> ACTIVE)
Given that "comment" equals "User verified identity via support"
And that "event" equals "unlockAccount"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ===========================================================================
# SUSPEND FROM ACTIVE -> DEACTIVATE FROM SUSPENDED
# ===========================================================================

Scenario: Admin suspends user for policy violation (ACTIVE -> SUSPENDED)
Given that "comment" equals "Policy violation"
And that "event" equals "suspend"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "SUSPENDED"

Scenario: Deactivate suspended user (SUSPENDED -> DEACTIVATED)
Given that "comment" equals "User requested full account deactivation"
And that "event" equals "deactivate"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DEACTIVATED"

# ===========================================================================
# DEACTIVATE FROM REGISTERED
# ===========================================================================

Scenario: Create a second user for deactivation from registered state
When I POST a REST request to URL "/user" with payload
"""json
{
    "description": "User to deactivate before verification",
    "firstName": "Delete",
    "lastName": "TestUser",
    "email": "delete@test.com"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "deleteId"
And the REST response key "mutatedEntity.currentState.stateId" is "REGISTERED"

Scenario: Deactivate unverified user (REGISTERED -> DEACTIVATED)
Given that "comment" equals "Admin deactivated unverified user"
And that "event" equals "deactivate"
When I PATCH a REST request to URL "/user/${deleteId}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${deleteId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DEACTIVATED"

# ===========================================================================
# INVALID: Wrong event on wrong state
# ===========================================================================

Scenario: Attempt to update profile on DEACTIVATED user -- should fail
Given that "event" equals "updateProfile"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "Trying to update deactivated user"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Send an invalid event to user -- should fail
When I PATCH a REST request to URL "/user/${deleteId}/nonExistentEvent" with payload
"""json
{
    "comment": "Invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

# ===========================================================================
# LOCK FROM ACTIVE, THEN DEACTIVATE FROM LOCKED
# ===========================================================================

Scenario: Create third user for lock-then-deactivate flow
When I POST a REST request to URL "/user" with payload
"""json
{
    "description": "User for lock-deactivate test",
    "firstName": "Lock",
    "lastName": "DeactivateUser",
    "email": "lockdeactivate@test.com"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "lockDeactivateId"
And the REST response key "mutatedEntity.currentState.stateId" is "REGISTERED"

Scenario: Verify email for lock-deactivate user
Given that "event" equals "verifyEmail"
When I PATCH a REST request to URL "/user/${lockDeactivateId}/${event}" with payload
"""json
{
    "comment": "Email verified"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "EMAIL_VERIFIED"

Scenario: Activate lock-deactivate user
Given that "event" equals "activate"
When I PATCH a REST request to URL "/user/${lockDeactivateId}/${event}" with payload
"""json
{
    "comment": "Activated"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Lock the lock-deactivate user
Given that "event" equals "lockAccount"
When I PATCH a REST request to URL "/user/${lockDeactivateId}/${event}" with payload
"""json
{
    "comment": "Failed logins"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "LOCKED"

Scenario: Deactivate locked user (LOCKED -> DEACTIVATED)
Given that "event" equals "deactivate"
When I PATCH a REST request to URL "/user/${lockDeactivateId}/${event}" with payload
"""json
{
    "comment": "Admin deactivating locked account"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${lockDeactivateId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DEACTIVATED"
