package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.CreateReactionRequest;
import ru.andryss.rutube.message.GetMyReactionResponse;
import ru.andryss.rutube.message.GetReactionsResponse;
import ru.andryss.rutube.service.ReactionService;

@Component
@RequiredArgsConstructor
public class ReactionInteractorImpl implements ReactionInteractor {

    private final ReactionService reactionService;

    @Override
    public void postApiReactions(CreateReactionRequest request) {
        // TODO: determine user
        String username = "username";

        reactionService.createReaction(request.getSourceId(), username, request.getReaction());
    }

    @Override
    public GetReactionsResponse getApiReactions(String sourceId) {
        GetReactionsResponse response = new GetReactionsResponse();
        response.setReactions(reactionService.getAllReactions(sourceId));
        return response;
    }

    @Override
    public GetMyReactionResponse getApiReactionsMy(String sourceId) {
        // TODO: determine user
        String username = "username";

        GetMyReactionResponse response = new GetMyReactionResponse();
        response.setReaction(reactionService.getMyReaction(sourceId, username).orElse(null));
        return response;
    }
}
