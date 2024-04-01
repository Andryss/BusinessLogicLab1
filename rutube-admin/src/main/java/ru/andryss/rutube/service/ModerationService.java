package ru.andryss.rutube.service;

import ru.andryss.rutube.message.AssignmentInfo;
import ru.andryss.rutube.message.ModerationInfo;
import ru.andryss.rutube.model.ModerationStatus;

import java.time.Instant;
import java.util.List;
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
    Optional<ModerationInfo> getNextModeration(String username);

    /**
     * Saves moderation result
     *
     * @param sourceId moderated source
     * @param username moderator
     * @param status moderation result
     * @param comment result comment (e.g. reject reason)
     */
    void uploadModeration(String sourceId, String username, ModerationStatus status, String comment);

    /**
     * Handle new moderation request
     *
     * @param sourceId source to moderate
     * @param downloadLink link to view source
     * @param createdAt request creation time
     */
    void handleRequest(String sourceId, String downloadLink, Instant createdAt);

    /**
     * Finds moderation request assigned before given timestamp
     *
     * @param timestamp timestamp to search
     * @return found moderation assignments
     */
    List<AssignmentInfo> findRequestsAssignedBefore(Instant timestamp);

    /**
     * Sends message to request moderation resending
     *
     * @param sourceId source to request
     */
    void requestResend(String sourceId);
}
