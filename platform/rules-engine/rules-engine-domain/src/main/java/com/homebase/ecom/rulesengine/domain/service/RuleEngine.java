package com.homebase.ecom.rulesengine.domain.service;

import com.homebase.ecom.rulesengine.domain.model.Rule;
import java.util.Map;

public interface RuleEngine {
    boolean execute(Rule rule, Map<String, Object> context);
}
