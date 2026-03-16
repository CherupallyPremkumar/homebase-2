package com.homebase.ecom.review.infrastructure.adapter;

import com.homebase.ecom.review.domain.port.ModerationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default moderation adapter that approves all content (logging only).
 * In production, replace with an adapter that calls an ML-based content moderation API.
 */
public class LoggingModerationAdapter implements ModerationPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingModerationAdapter.class);

    @Override
    public ModerationResult moderate(String content) {
        log.debug("Content moderation check for text of length {}", content != null ? content.length() : 0);
        // Default: approve all. Production adapter would call external API.
        return ModerationResult.approvedResult();
    }
}
