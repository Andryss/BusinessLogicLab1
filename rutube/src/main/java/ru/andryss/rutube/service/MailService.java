package ru.andryss.rutube.service;

import ru.andryss.rutube.model.VideoStatus;

import java.util.Map;

/**
 * Service for working with emails
 */
public interface MailService {
    /**
     * Sends user notification about videos pending his actions
     *
     * @param author user to send
     * @param email user email
     * @param counts number of videos in pending statuses
     */
    void sendVideosPendingActionsNotification(String author, String email, Map<VideoStatus, Long> counts);
}
