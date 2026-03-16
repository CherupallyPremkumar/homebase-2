package com.homebase.ecom.review.domain.port;

/**
 * Outbound port for content moderation.
 * Abstracts the underlying moderation engine (e.g., ML-based content analysis,
 * external API, or simple word-list approach).
 */
public interface ModerationPort {

    /**
     * Checks if the given text content passes moderation rules.
     *
     * @param content The text content to moderate
     * @return ModerationResult with the decision and reason
     */
    ModerationResult moderate(String content);

    /**
     * Represents the result of a content moderation check.
     */
    record ModerationResult(boolean approved, String reason, double confidenceScore) {
        public static ModerationResult approvedResult() {
            return new ModerationResult(true, "Content approved", 1.0);
        }

        public static ModerationResult rejected(String reason, double score) {
            return new ModerationResult(false, reason, score);
        }
    }
}
