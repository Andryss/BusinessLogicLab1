package ru.andryss.rutube.interactor;

import org.springframework.data.domain.PageRequest;
import ru.andryss.rutube.message.*;
import ru.andryss.rutube.security.CustomUserDetails;

/**
 * Interactor for handling video requests
 */
public interface VideoInteractor {
    /**
     * Handles POST /api/videos:new request
     */
    NewVideoResponse postApiVideosNew(String prototype, CustomUserDetails user);
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
    void putApiVideos(String sourceId, PutVideoRequest request, CustomUserDetails user);
    /**
     * Handles GET /api/videos/{sourceId}/status request
     */
    GetVideoStatusResponse getApiVideosStatus(String sourceId, CustomUserDetails user);
    /**
     * Handles POST /api/videos/{sourceId}:publish request
     */
    void postApiVideosPublish(String sourceId, CustomUserDetails user);
}
