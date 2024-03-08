package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.CreateReactionRequest;
import ru.andryss.rutube.message.GetMyReactionResponse;
import ru.andryss.rutube.message.GetReactionsResponse;
import ru.andryss.rutube.security.CustomUserDetails;
import ru.andryss.rutube.service.ReactionService;

@Component
@RequiredArgsConstructor
public class ReactionInteractorImpl implements ReactionInteractor {

    private final ReactionService reactionService;

    @Override
    public void postApiReactions(CreateReactionRequest request, CustomUserDetails user) {
        reactionService.createReaction(request.getSourceId(), user.getUsername(), request.getReaction());
    }

    @Override
    public GetReactionsResponse getApiReactions(String sourceId) {
        GetReactionsResponse response = new GetReactionsResponse();
        response.setReactions(reactionService.getAllReactions(sourceId));
        return response;
    }

    @Override
    public GetMyReactionResponse getApiReactionsMy(String sourceId, CustomUserDetails user) {
        GetMyReactionResponse response = new GetMyReactionResponse();
        response.setReaction(reactionService.getMyReaction(sourceId, user.getUsername()).orElse(null));
        return response;
    }
}
