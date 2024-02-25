package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andryss.rutube.interactor.ReactionInteractor;
import ru.andryss.rutube.message.CreateReactionRequest;
import ru.andryss.rutube.message.GetMyReactionResponse;
import ru.andryss.rutube.message.GetReactionsResponse;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionInteractor interactor;

    @PostMapping("/api/reactions")
    @ResponseStatus(NO_CONTENT)
    public void postApiReactions(
            @RequestBody @Valid CreateReactionRequest request,
            @AuthenticationPrincipal User user
    ) {
        interactor.postApiReactions(request, user);
    }

    @GetMapping("/api/reactions")
    @ResponseStatus(OK)
    public GetReactionsResponse getApiReactions(
            @RequestParam @NotBlank String sourceId
    ) {
        return interactor.getApiReactions(sourceId);
    }

    @GetMapping("/api/reactions/my")
    @ResponseStatus(OK)
    public GetMyReactionResponse getApiReactionsMy(
            @RequestParam @NotBlank String sourceId,
            @AuthenticationPrincipal User user
    ) {
        return interactor.getApiReactionsMy(sourceId, user);
    }
}
