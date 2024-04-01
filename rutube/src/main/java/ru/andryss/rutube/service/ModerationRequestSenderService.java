package ru.andryss.rutube.service;

public interface ModerationRequestSenderService {
    /**
     * Sends moderation request for the first time
     *
     * @param sourceId source to moderate
     * @param downloadLink source link
     */
    void sendFirstTime(String sourceId, String downloadLink);

    /**
     * Resends moderation request if resend limit is not exceeded
     *
     * @param sourceId source to moderate
     * @param downloadLink source link
     */
    void resend(String sourceId, String downloadLink);

    /**
     * Clears source resending info
     *
     * @param sourceId source to clear
     */
    void clearResendInfo(String sourceId);
}
