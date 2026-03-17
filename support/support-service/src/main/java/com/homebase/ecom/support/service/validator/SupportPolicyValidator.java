package com.homebase.ecom.support.service.validator;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Policy validator for support ticket operations.
 * Reads configurable rules from cconfig (org/chenile/config/support.json).
 *
 * Validates:
 * - Max open tickets per customer
 * - SLA response/resolution hours
 * - Max reopen count
 * - Escalation thresholds
 */
@Component
public class SupportPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(SupportPolicyValidator.class);

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Ticket Creation
    // ═══════════════════════════════════════════════════════════════════════

    public void validateTicketCreation(String customerId, long currentOpenTicketCount) {
        JsonNode config = getSupportConfig();
        int maxOpen = configInt(config, "/policies/maxOpenTicketsPerCustomer", 10);

        if (currentOpenTicketCount >= maxOpen) {
            throw new IllegalArgumentException(
                    "Customer " + customerId + " has reached the maximum of " + maxOpen + " open tickets");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Reopen
    // ═══════════════════════════════════════════════════════════════════════

    public void validateReopen(SupportTicket ticket) {
        JsonNode config = getSupportConfig();
        int maxReopens = configInt(config, "/policies/maxReopens", 3);

        if (ticket.getReopenCount() >= maxReopens) {
            throw new IllegalArgumentException(
                    "Ticket " + ticket.getId() + " has reached the maximum of " + maxReopens + " reopens");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // RULES: SLA
    // ═══════════════════════════════════════════════════════════════════════

    public int getSlaResponseHours() {
        JsonNode config = getSupportConfig();
        return configInt(config, "/sla/slaResponseHours", 24);
    }

    public int getSlaResolutionHours() {
        JsonNode config = getSupportConfig();
        return configInt(config, "/sla/slaResolutionHours", 72);
    }

    public int getAutoCloseAfterDays() {
        JsonNode config = getSupportConfig();
        return configInt(config, "/sla/autoCloseAfterDays", 7);
    }

    public int getEscalationAfterHours() {
        JsonNode config = getSupportConfig();
        return configInt(config, "/sla/escalationAfterHours", 48);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Config helpers
    // ═══════════════════════════════════════════════════════════════════════

    private JsonNode getSupportConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value("support", null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load support.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    private int configInt(JsonNode config, String path, int defaultVal) {
        JsonNode node = config.at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultVal;
    }
}
