package ru.andryss.rutube.interactor;

import org.springframework.security.core.userdetails.User;
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
    NewVideoResponse postApiVideosNew(String prototype, User user);
    /**
     * Handles PUT /api/videos/{sourceId} request
     */
    void putApiVideos(String sourceId, PutVideoRequest request, User user);
    /**
     * Handles GET /api/videos/{sourceId}/status request
     */
    GetStatusResponse getApiVideosStatus(String sourceId, User user);
    /**
     * Handles POST /api/videos/{sourceId}:publish request
     */
    void postApiVideosPublish(String sourceId, User user);
}
