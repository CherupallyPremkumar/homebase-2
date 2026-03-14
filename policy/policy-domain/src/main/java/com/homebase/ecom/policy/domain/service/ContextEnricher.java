package com.homebase.ecom.policy.domain.service;

import com.homebase.ecom.policy.api.dto.ContextDto;
import java.util.Map;

public interface ContextEnricher {
    Map<String, Object> enrich(ContextDto contextDto);
}
