package com.homebase.ecom.support.infrastructure.integration;

import com.homebase.ecom.support.domain.port.AgentAssignmentPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter for agent assignment operations.
 *
 * Currently a logging stub — will delegate to an external workforce
 * management system once integration is complete.
 */
public class LoggingAgentAssignmentAdapter implements AgentAssignmentPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingAgentAssignmentAdapter.class);

    @Override
    public String findAvailableAgent(String category, String priority) {
        log.info("Finding available agent: category={}, priority={}", category, priority);
        return null;
    }

    @Override
    public boolean isAgentAvailable(String agentId) {
        log.info("Checking agent availability: agentId={}", agentId);
        return false;
    }
}
