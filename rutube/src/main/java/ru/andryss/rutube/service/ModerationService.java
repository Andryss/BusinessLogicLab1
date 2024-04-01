package ru.andryss.rutube.service;

import ru.andryss.rutube.model.ModerationStatus;

import java.time.Instant;

/**
 * Service for working with moderation
 */
public interface ModerationService {
    /**
     * Handle new moderation result
     *
     * @param sourceId moderated source
     * @param status moderation result
     * @param comment comment
     * @param createdAt creation timestamp
     */
    void handleResult(String sourceId, ModerationStatus status, String comment, Instant createdAt);

    /**
     * Handle moderation resend
     *
     * @param sourceId source to resend
     */
    void handleResend(String sourceId);

    /**
     * Find comment of moderation result
     *
     * @param sourceId source to search
     * @return moderation comment
     */
    String getModerationComment(String sourceId);
}
