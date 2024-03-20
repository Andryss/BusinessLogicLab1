package ru.andryss.rutube.interactor;

import org.springframework.security.core.userdetails.User;
import ru.andryss.rutube.message.GetNextModerationResponse;
import ru.andryss.rutube.message.UploadModerationResultRequest;
import ru.andryss.rutube.security.CustomUserDetails;

/**
 * Interactor for handling moderation requests
 */
public interface ModerationInteractor {
    /**
     * Handles GET /api/moderation/next request
     */
    GetNextModerationResponse getApiModerationNext(CustomUserDetails user);

    /**
     * Handles POST /api/moderation request
     */
    void postApiModeration(UploadModerationResultRequest request, CustomUserDetails user);
}
