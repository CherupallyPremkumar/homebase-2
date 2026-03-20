package com.homebase.ecom.notification.service.validator;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Central policy validator for notification operations.
 *
 * <p>Two-layer validation:
 * <ul>
 *   <li><b>Layer 1 — Cconfig thresholds:</b> reads configurable rules from
 *       {@code org/chenile/config/notification.json} (overridable per tenant in DB).
 *       Used for channel validation, rate limiting, template checks, etc.</li>
 *   <li><b>Layer 2 — Business rules:</b> enforces domain invariants like
 *       unsubscribe checks, supported channels, and template requirements.</li>
 * </ul>
 */
public class NotificationPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(NotificationPolicyValidator.class);

    private static final List<String> DEFAULT_SUPPORTED_CHANNELS = Arrays.asList("EMAIL", "SMS", "PUSH", "IN_APP");

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Channel Validation
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that the notification channel is supported.
     */
    public void validateChannel(String channel) {
        List<String> supported = getSupportedChannels();
        if (channel == null || channel.trim().isEmpty()) {
            throw new IllegalArgumentException("Notification channel is required");
        }
        if (!supported.contains(channel.toUpperCase())) {
            throw new IllegalArgumentException("Unsupported notification channel: " + channel
                    + ". Supported channels: " + supported);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Rate Limiting
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Returns the rate limit per minute (for external enforcement).
     */
    public int getRateLimitPerMinute() {
        JsonNode config = getNotificationConfig();
        return configInt(config, "/policies/rateLimitPerMinute", 1000);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Template Check
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that a template is provided when template requirement is enabled.
     */
    public void validateTemplate(Notification notification) {
        JsonNode config = getNotificationConfig();
        boolean templateRequired = configBool(config, "/policies/templateRequired", true);
        if (templateRequired) {
            if ((notification.getTemplateId() == null || notification.getTemplateId().trim().isEmpty())
                    && (notification.getBody() == null || notification.getBody().trim().isEmpty())) {
                throw new IllegalArgumentException("Either templateId or body must be provided (templateRequired=true)");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Unsubscribe Check
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Checks if the customer has unsubscribed from this channel.
     * In production, this would query a preference store.
     */
    public void validateNotUnsubscribed(Notification notification) {
        // Placeholder for production integration with customer preference service
        // If customer has unsubscribed from this channel, throw exception
        log.debug("Unsubscribe check for customerId={}, channel={}",
                notification.getCustomerId(), notification.getChannel());
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Retry Configuration
    // ═══════════════════════════════════════════════════════════════════════

    public int getMaxRetryAttempts() {
        JsonNode config = getNotificationConfig();
        return configInt(config, "/policies/maxRetryAttempts", 3);
    }

    public int getRetryDelayMinutes() {
        JsonNode config = getNotificationConfig();
        return configInt(config, "/policies/retryDelayMinutes", 5);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Batch Configuration
    // ═══════════════════════════════════════════════════════════════════════

    public int getBatchSize() {
        JsonNode config = getNotificationConfig();
        return configInt(config, "/policies/batchSize", 100);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Full Queue Validation
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Full validation before queueing a notification for delivery.
     * Called by QueueNotificationAction.
     */
    public void validateForQueue(Notification notification) {
        validateChannel(notification.getChannel());
        validateTemplate(notification);
        validateNotUnsubscribed(notification);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Config helpers
    // ═══════════════════════════════════════════════════════════════════════

    @SuppressWarnings("unchecked")
    private List<String> getSupportedChannels() {
        JsonNode config = getNotificationConfig();
        JsonNode channelsNode = config.at("/policies/supportedChannels");
        if (!channelsNode.isMissingNode() && channelsNode.isArray()) {
            List<String> channels = new java.util.ArrayList<>();
            channelsNode.forEach(node -> channels.add(node.asText().toUpperCase()));
            return channels;
        }
        return DEFAULT_SUPPORTED_CHANNELS;
    }

    private JsonNode getNotificationConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value("notification", null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load notification.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    private int configInt(JsonNode config, String path, int defaultVal) {
        JsonNode node = config.at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultVal;
    }

    private boolean configBool(JsonNode config, String path, boolean defaultVal) {
        JsonNode node = config.at(path);
        return node.isMissingNode() ? defaultVal : node.asBoolean(defaultVal);
    }
}
