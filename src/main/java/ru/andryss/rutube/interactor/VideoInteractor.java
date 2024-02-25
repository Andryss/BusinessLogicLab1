package ru.andryss.rutube.interactor;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import ru.andryss.rutube.message.*;

/**
 * Interactor for handling video requests
 */
public interface VideoInteractor {
    /**
     * Handles POST /api/videos:new request
     */
    NewVideoResponse postApiVideosNew(String prototype, User user);
    /**
     * Handles GET /api/videos request
     */
    GetVideosResponse getApiVideos(PageRequest pageRequest);
    /**
     * Handles GET /api/video/{sourceId} request
     */
    GetVideoResponse getApiVideo(String sourceId);
    /**
     * Handles PUT /api/videos/{sourceId} request
     */
    void putApiVideos(String sourceId, PutVideoRequest request, User user);
    /**
     * Handles GET /api/videos/{sourceId}/status request
     */
    GetVideoStatusResponse getApiVideosStatus(String sourceId, User user);
    /**
     * Handles POST /api/videos/{sourceId}:publish request
     */
    void postApiVideosPublish(String sourceId, User user);
}
