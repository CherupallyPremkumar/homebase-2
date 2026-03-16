package com.homebase.ecom.support.domain.port;

/**
 * Hexagonal port for agent assignment logic.
 * Infrastructure adapter can integrate with external workforce management.
 */
public interface AgentAssignmentPort {

    /**
     * Find the next available agent for the given category and priority.
     * @return agent ID or null if none available
     */
    String findAvailableAgent(String category, String priority);

    /**
     * Check if a specific agent is available for assignment.
     */
    boolean isAgentAvailable(String agentId);
}
