package com.homebase.ecom.rulesengine.domain.service;

import com.homebase.ecom.rulesengine.domain.model.Rule;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import org.chenile.base.exception.BadRequestException;

import java.util.List;
import java.util.Set;

/**
 * Validates rules-engine entities using Chenile i18n error codes.
 * Error codes 11001-11099 are reserved for rules-engine module.
 * Messages resolved from messages.properties via Chenile's MultipleMessageSource.
 */
public class RulesEngineValidator {

    // ================================================================
    // Error Code Constants (rules-engine range: 11001–11099)
    // ================================================================

    public static final int RULESET_NAME_REQUIRED = 11001;
    public static final int RULESET_NAME_TOO_LONG = 11002;
    public static final int TARGET_MODULE_REQUIRED = 11003;
    public static final int RULESET_MUST_HAVE_RULES = 11004;
    public static final int RULE_NAME_REQUIRED = 11005;
    public static final int RULE_EXPRESSION_REQUIRED = 11006;
    public static final int RULE_EXPRESSION_INVALID = 11007;
    public static final int RULE_UNKNOWN_FACT = 11008;
    public static final int COMMENT_REQUIRED = 11009;
    public static final int DEFAULT_EFFECT_REQUIRED = 11010;

    private final int nameMaxLength;

    public RulesEngineValidator(int nameMaxLength) {
        this.nameMaxLength = nameMaxLength;
    }

    // ================================================================
    // RuleSet Validations
    // ================================================================

    /**
     * Validates a RuleSet on create (DRAFT entry).
     * Checks: name required, name max length, targetModule required, defaultEffect required.
     */
    public void validateForCreate(RuleSet ruleSet) {
        requireNonBlank(ruleSet.getName(), RULESET_NAME_REQUIRED);
        requireMaxLength(ruleSet.getName(), nameMaxLength, RULESET_NAME_TOO_LONG);
        requireNonBlank(ruleSet.getTargetModule(), TARGET_MODULE_REQUIRED);
        if (ruleSet.getDefaultEffect() == null) {
            throw new BadRequestException(DEFAULT_EFFECT_REQUIRED, new Object[]{});
        }
    }

    /**
     * Validates a RuleSet before submission for review or direct activation.
     * Checks: all create validations + must have rules + each rule valid.
     */
    public void validateForSubmit(RuleSet ruleSet) {
        validateForCreate(ruleSet);
        if (ruleSet.getRules() == null || ruleSet.getRules().isEmpty()) {
            throw new BadRequestException(RULESET_MUST_HAVE_RULES, new Object[]{});
        }
        for (Rule rule : ruleSet.getRules()) {
            validateRule(rule);
        }
    }

    /**
     * Validates an individual rule: name + expression required.
     */
    public void validateRule(Rule rule) {
        requireNonBlank(rule.getName(), RULE_NAME_REQUIRED);
        requireNonBlank(rule.getExpression(), RULE_EXPRESSION_REQUIRED);
    }

    /**
     * Validates that a SpEL expression is parseable.
     * Called by actions that have access to the SpEL parser (infrastructure concern).
     */
    public void validateExpressionParseable(String ruleName, String parseError) {
        throw new BadRequestException(RULE_EXPRESSION_INVALID, new Object[]{ruleName, parseError});
    }

    /**
     * Validates that a fact reference in a rule is known.
     */
    public void validateFactReference(String factName, Set<String> validFacts) {
        if (!validFacts.contains(factName)) {
            throw new BadRequestException(RULE_UNKNOWN_FACT, new Object[]{factName, String.join(", ", validFacts)});
        }
    }

    /**
     * Validates that a comment is provided for actions that require it.
     */
    public void validateCommentRequired(String comment, String eventName) {
        if (comment == null || comment.isBlank()) {
            throw new BadRequestException(COMMENT_REQUIRED, new Object[]{eventName});
        }
    }

    // ================================================================
    // Config Getters
    // ================================================================

    public int getNameMaxLength() { return nameMaxLength; }

    // ================================================================
    // Private Helpers
    // ================================================================

    private void requireNonBlank(String value, int errorCode) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(errorCode, new Object[]{});
        }
    }

    private void requireMaxLength(String value, int maxLength, int errorCode) {
        if (value != null && value.length() > maxLength) {
            throw new BadRequestException(errorCode, new Object[]{maxLength});
        }
    }
}
