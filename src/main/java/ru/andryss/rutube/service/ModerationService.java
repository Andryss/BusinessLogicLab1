package ru.andryss.rutube.service;

import ru.andryss.rutube.model.ModerationStatus;

import java.util.Optional;

/**
 * Service for working with moderation
 */
public interface ModerationService {
    /**
     * Assigns next source to moderate
     *
     * @param username moderator
     * @return assigned source (or previous if wasn't moderated)
     */
    Optional<String> getNextModeration(String username);

    /**
     * Saves moderation result
     *
     * @param sourceId moderated source
     * @param username moderator
     * @param status moderation result
     * @param comment result comment (e.g. reject reason)
     */
    void uploadModeration(String sourceId, String username, ModerationStatus status, String comment);
}
