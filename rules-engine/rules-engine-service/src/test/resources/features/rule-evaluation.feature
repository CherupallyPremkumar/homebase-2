Feature: Policy Evaluation Logic
  As a system administrator
  I want to evaluate policies against contexts
  So that I can determine access control decisions

  Scenario: Policy allows access when all rules pass
    Given a policy "P1" exists with default effect "DENY"
    And rule "R1" with expression "#tenantId == 'T1'" and effect "ALLOW" is added to "P1"
    When I evaluate policy "P1" with context:
      | tenantId | T1 |
      | userId   | U1 |
    Then the decision should be "ALLOWED"
    And the reason should contain "Rule matched: R1"

  Scenario: Policy denies access when a rule fails
    Given a policy "P2" exists with default effect "DENY"
    And rule "R2" with expression "#tenantId == 'T2'" and effect "ALLOW" is added to "P2"
    When I evaluate policy "P2" with context:
      | tenantId | T1 |
      | userId   | U1 |
    Then the decision should be "DENIED"
    And the reason should contain "Default effect applied: DENY"

  Scenario: Policy denies access when a DENY rule matches (Short-circuit)
    Given a policy "P3" exists with default effect "ALLOW"
    And rule "R3" with expression "#userId == 'U1'" and effect "DENY" is added to "P3"
    And rule "R4" with expression "true" and effect "ALLOW" is added to "P3"
    When I evaluate policy "P3" with context:
      | userId | U1 |
    Then the decision should be "DENIED"
    And the reason should contain "Rule matched: R3 (DENY)"
