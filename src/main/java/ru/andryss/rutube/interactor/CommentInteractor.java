package ru.andryss.rutube.interactor;

import org.springframework.data.domain.PageRequest;
import ru.andryss.rutube.message.CreateCommentRequest;
import ru.andryss.rutube.message.GetCommentsResponse;
import ru.andryss.rutube.security.CustomUserDetails;

/**
 * Interactor for handling comment requests
 */
public interface CommentInteractor {
    /**
     * Handles POST /api/comments request
     */
    void postApiComments(CreateCommentRequest request, CustomUserDetails user);

    /**
     * Handles GET /api/comments request
     */
    GetCommentsResponse getApiComments(String sourceId, String parentId, PageRequest pageRequest);
}
