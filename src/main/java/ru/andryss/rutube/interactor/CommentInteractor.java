package ru.andryss.rutube.interactor;

import ru.andryss.rutube.message.CreateCommentRequest;
import ru.andryss.rutube.message.GetCommentsResponse;

/**
 * Interactor for handling comment request
 */
public interface CommentInteractor {
    /**
     * Handles POST /api/comments request
     */
    void postApiComments(CreateCommentRequest request);

    /**
     * Handles GET /api/comments request
     */
    GetCommentsResponse getApiComments(String sourceId);
}
