package ru.andryss.rutube.interactor;

import org.springframework.security.core.userdetails.User;
import ru.andryss.rutube.message.CreateReactionRequest;
import ru.andryss.rutube.message.GetMyReactionResponse;
import ru.andryss.rutube.message.GetReactionsResponse;
import ru.andryss.rutube.security.CustomUserDetails;

/**
 * Interactor for handling reaction requests
 */
public interface ReactionInteractor {
    /**
     * Handles POST /api/reactions request
     */
    void postApiReactions(CreateReactionRequest request, CustomUserDetails user);
    /**
     * Handles GET /api/reactions request
     */
    GetReactionsResponse getApiReactions(String sourceId);
    /**
     * Handles GET /api/reactions/my request
     */
    GetMyReactionResponse getApiReactionsMy(String sourceId, CustomUserDetails user);
}
