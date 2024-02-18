package ru.andryss.rutube.interactor;

import ru.andryss.rutube.message.GetStatusResponse;
import ru.andryss.rutube.message.NewVideoResponse;
import ru.andryss.rutube.message.PutVideoRequest;

/**
 * Interactor for handling video requests
 */
public interface VideoInteractor {
    /**
     * Handles POST /api/videos:new request
     */
    NewVideoResponse postApiVideosNew();
    /**
     * Handles PUT /api/videos/{sourceId} request
     */
    void putApiVideos(String sourceId, PutVideoRequest request);
    /**
     * Handles GET /api/videos/{sourceId}/status request
     */
    GetStatusResponse getApiVideosStatus(String sourceId);
    /**
     * Handles POST /api/videos/{sourceId}:publish request
     */
    void postApiVideosPublish(String sourceId);
}
