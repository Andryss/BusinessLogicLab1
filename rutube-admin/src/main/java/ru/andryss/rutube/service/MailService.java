package ru.andryss.rutube.service;

/**
 * Service for working with emails
 */
public interface MailService {
    /**
     * Sends moderator notification about moderation pending his action
     *
     * @param moderator moderator to send
     * @param email moderator email
     * @param sourceId source pending moderation
     */
    void sendModerationPendingNotification(String moderator, String email, String sourceId);
}
