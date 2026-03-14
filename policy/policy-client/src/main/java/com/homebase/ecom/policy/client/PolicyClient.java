package com.homebase.ecom.policy.client;

import com.homebase.ecom.policy.api.dto.EvaluateRequest;
import com.homebase.ecom.policy.api.dto.DecisionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for the Policy Bounded Context.
 */
@FeignClient(name = "policy-service", url = "${homebase.policy.url:http://localhost:8085}")
public interface PolicyClient {

    @PostMapping("/policy/evaluate")
    DecisionDto evaluate(@RequestBody EvaluateRequest request);
}
