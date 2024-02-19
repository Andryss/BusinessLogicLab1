package ru.andryss.rutube.service;

import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.model.VideoAccess;
import ru.andryss.rutube.model.VideoCategory;
import ru.andryss.rutube.model.VideoStatus;

/**
 * Service for working with videos
 */
public interface VideoService {
    /**
     * Creates new video in draft state
     *
     * @param sourceId uploaded video identifier
     * @param author uploading user
     * @param prototype video to copy its info into new video
     */
    void createNewVideo(String sourceId, String author, String prototype);

    /**
     * Puts video info by given identifier
     *
     * @param sourceId video to put info
     * @param author putting user
     * @param videoInfo video info to put
     */
    void putVideo(String sourceId, String author, VideoInfo videoInfo);

    /**
     * Returns status of given video
     *
     * @param sourceId video to search
     * @param author getting user
     * @return status
     */
    VideoStatus getVideoStatus(String sourceId, String author);

    /**
     * Publishes given video
     *
     * @param sourceId video to publish
     * @param author publishing user
     */
    void publishVideo(String sourceId, String author);

    /**
     * Finds published video
     *
     * @param sourceId video to search
     * @return found video
     */
    Video findPublishedVideo(String sourceId);

    record VideoInfo(String title, String description, VideoCategory category, VideoAccess access,
                     Boolean ageRestriction, Boolean comments) { }
}
