package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andryss.rutube.interactor.ReactionInteractor;
import ru.andryss.rutube.message.CreateReactionRequest;
import ru.andryss.rutube.message.GetMyReactionResponse;
import ru.andryss.rutube.message.GetReactionsResponse;
import ru.andryss.rutube.security.CustomUserDetails;

@Validated
@RestController
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionInteractor interactor;

    @PostMapping("/api/reactions")
    public void postApiReactions(
            @RequestBody @Valid CreateReactionRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        interactor.postApiReactions(request, user);
    }

    @GetMapping("/api/reactions")
    public GetReactionsResponse getApiReactions(
            @RequestParam @NotBlank String sourceId
    ) {
        return interactor.getApiReactions(sourceId);
    }

    @GetMapping("/api/reactions/my")
    public GetMyReactionResponse getApiReactionsMy(
            @RequestParam @NotBlank String sourceId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return interactor.getApiReactionsMy(sourceId, user);
    }
}
