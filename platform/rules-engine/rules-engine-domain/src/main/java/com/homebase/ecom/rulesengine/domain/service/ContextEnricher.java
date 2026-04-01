package com.homebase.ecom.rulesengine.domain.service;

import com.homebase.ecom.rulesengine.api.dto.ContextDto;
import java.util.Map;

public interface ContextEnricher {
    Map<String, Object> enrich(ContextDto contextDto);
}
