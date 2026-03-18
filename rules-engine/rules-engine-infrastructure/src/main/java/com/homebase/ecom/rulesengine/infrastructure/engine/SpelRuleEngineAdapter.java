package com.homebase.ecom.rulesengine.infrastructure.engine;

import com.homebase.ecom.rulesengine.domain.model.Rule;
import com.homebase.ecom.rulesengine.domain.service.RuleEngine;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.ConstructorResolver;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.TypeLocator;
import org.springframework.expression.EvaluationContext;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpelRuleEngineAdapter implements RuleEngine {
    private static final Logger log = LoggerFactory.getLogger(SpelRuleEngineAdapter.class);
    private final ExpressionParser parser = new SpelExpressionParser();

    // Patterns that indicate dangerous SpEL constructs
    private static final Pattern DANGEROUS_PATTERN = Pattern.compile(
            "(?i)(T\\s*\\()" +                    // T() type references
            "|(new\\s+[A-Z])" +                    // new ClassName() constructors
            "|(getClass\\s*\\()" +                 // .getClass() reflection
            "|(forName\\s*\\()" +                  // Class.forName()
            "|(Runtime)" +                          // Runtime access
            "|(ProcessBuilder)" +                   // ProcessBuilder access
            "|(exec\\s*\\()" +                     // exec() calls
            "|(invoke\\s*\\()" +                   // Method.invoke() reflection
            "|(getRuntime\\s*\\()" +               // Runtime.getRuntime()
            "|(java\\.lang\\.)" +                  // Direct java.lang references
            "|(javax?\\.)" +                       // Java package references
            "|(org\\.springframework\\.)" +        // Spring internals
            "|(\\{\\s*[^}]*\\.class)"              // .class references
    );

    @Override
    public boolean execute(Rule rule, Map<String, Object> contextData) {
        try {
            String expression = rule.getExpression();

            // Pre-parse validation: reject dangerous expressions
            if (DANGEROUS_PATTERN.matcher(expression).find()) {
                log.error("SECURITY: Blocked dangerous SpEL expression in rule '{}': {}", rule.getName(), expression);
                return false;
            }

            Expression exp = parser.parseExpression(expression);
            StandardEvaluationContext context = createSandboxedContext(contextData);
            Boolean result = exp.getValue(context, Boolean.class);
            return result != null && result;
        } catch (Exception e) {
            log.error("Error executing rule: {}", rule.getName(), e);
            return false;
        }
    }

    private StandardEvaluationContext createSandboxedContext(Map<String, Object> contextData) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(contextData);

        // Block type references — no T(java.lang.Runtime) etc.
        context.setTypeLocator(typeName -> {
            throw new SecurityException("Type references are not allowed in rule expressions: " + typeName);
        });

        // Block constructor calls — no new ProcessBuilder() etc.
        context.setConstructorResolvers(Collections.singletonList(
                (ctx, typeName, argumentTypes) -> {
                    throw new SecurityException("Constructor calls are not allowed in rule expressions: " + typeName);
                }
        ));

        return context;
    }
}
