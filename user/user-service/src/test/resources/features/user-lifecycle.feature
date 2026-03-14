Feature: User Lifecycle — tests the full user state machine through all paths.
  Covers registration, email verification, resend verification, profile updates,
  address management, account locking/unlocking, suspension/reinstatement, and deletion.
  States: PENDING_VERIFICATION -> ACTIVE <-> LOCKED / SUSPENDED -> DELETED

# ═══════════════════════════════════════════════════════════════════════════════
# HAPPY PATH: Register -> Resend Verification -> Verify Email -> Active
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Register a new user in PENDING_VERIFICATION state
Given that "flowName" equals "user-flow"
And that "initialState" equals "PENDING_VERIFICATION"
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
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_VERIFICATION"

Scenario: Resend verification email (stays PENDING_VERIFICATION)
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
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_VERIFICATION"

Scenario: Verify email and activate (PENDING_VERIFICATION -> ACTIVE)
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
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# PROFILE MANAGEMENT: Update profile, Add address, Remove address
# ═══════════════════════════════════════════════════════════════════════════════

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
Given that "comment" equals "Adding home address"
And that "event" equals "addAddress"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Change password (ACTIVE -> ACTIVE)
Given that "comment" equals "User changed password"
And that "event" equals "changePassword"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

# ═══════════════════════════════════════════════════════════════════════════════
# LOCK/UNLOCK FLOW: ACTIVE -> LOCKED -> ACTIVE
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Lock account after failed login attempts (ACTIVE -> LOCKED)
Given that "comment" equals "3 consecutive failed login attempts detected"
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

# ═══════════════════════════════════════════════════════════════════════════════
# SUSPEND/REINSTATE FLOW: ACTIVE -> SUSPENDED -> ACTIVE
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Admin suspends user for fraud (ACTIVE -> SUSPENDED)
Given that "comment" equals "Fraud detected"
And that "event" equals "suspendUser"
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

# ═══════════════════════════════════════════════════════════════════════════════
# DELETE FLOW: ACTIVE -> DELETED
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Soft delete active user account (ACTIVE -> DELETED)
Given that "comment" equals "User requested full account deletion"
And that "event" equals "deleteAccount"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELETED"

# ═══════════════════════════════════════════════════════════════════════════════
# DELETE FROM PENDING_VERIFICATION
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create a second user for deletion from pending state
When I POST a REST request to URL "/user" with payload
"""json
{
    "description": "User to delete before verification",
    "firstName": "Delete",
    "lastName": "TestUser",
    "email": "delete@test.com"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "deleteId"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_VERIFICATION"

Scenario: Delete unverified user (PENDING_VERIFICATION -> DELETED)
Given that "comment" equals "Admin deleted unverified user"
And that "event" equals "deleteAccount"
When I PATCH a REST request to URL "/user/${deleteId}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${deleteId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELETED"

# ═══════════════════════════════════════════════════════════════════════════════
# INVALID: Wrong event on wrong state
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Attempt to update profile on DELETED user — should fail
Given that "event" equals "updateProfile"
When I PATCH a REST request to URL "/user/${id}/${event}" with payload
"""json
{
    "comment": "Trying to update deleted user"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

Scenario: Send an invalid event to user — should fail
When I PATCH a REST request to URL "/user/${deleteId}/nonExistentEvent" with payload
"""json
{
    "comment": "Invalid event"
}
"""
Then the REST response does not contain key "mutatedEntity"
And the http status code is 422

# ═══════════════════════════════════════════════════════════════════════════════
# SUSPEND FROM LOCKED: Create user, activate, lock, then delete from locked
# ═══════════════════════════════════════════════════════════════════════════════

Scenario: Create third user for lock-then-delete flow
When I POST a REST request to URL "/user" with payload
"""json
{
    "description": "User for lock-delete test",
    "firstName": "Lock",
    "lastName": "DeleteUser",
    "email": "lockdelete@test.com"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "lockDeleteId"
And the REST response key "mutatedEntity.currentState.stateId" is "PENDING_VERIFICATION"

Scenario: Verify email for lock-delete user
Given that "event" equals "verifyEmail"
When I PATCH a REST request to URL "/user/${lockDeleteId}/${event}" with payload
"""json
{
    "comment": "Email verified"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "ACTIVE"

Scenario: Lock the lock-delete user
Given that "event" equals "lockAccount"
When I PATCH a REST request to URL "/user/${lockDeleteId}/${event}" with payload
"""json
{
    "comment": "Failed logins"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.currentState.stateId" is "LOCKED"

Scenario: Delete locked user (LOCKED -> DELETED)
Given that "event" equals "deleteAccount"
When I PATCH a REST request to URL "/user/${lockDeleteId}/${event}" with payload
"""json
{
    "comment": "Admin deleting locked account"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${lockDeleteId}"
And the REST response key "mutatedEntity.currentState.stateId" is "DELETED"
