package com.homebase.ecom.policy.domain.service;

import com.homebase.ecom.policy.domain.model.Rule;
import java.util.Map;

public interface RuleEngine {
    boolean execute(Rule rule, Map<String, Object> context);
}
