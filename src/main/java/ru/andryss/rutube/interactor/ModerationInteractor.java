package ru.andryss.rutube.interactor;

import org.springframework.security.core.userdetails.User;
import ru.andryss.rutube.message.GetNextModerationResponse;
import ru.andryss.rutube.message.UploadModerationResultRequest;

/**
 * Interactor for handling moderation requests
 */
public interface ModerationInteractor {
    /**
     * Handles GET /api/moderation/next request
     */
    GetNextModerationResponse getApiModerationNext(User user);

    /**
     * Handles POST /api/moderation request
     */
    void postApiModeration(UploadModerationResultRequest request, User user);
}
