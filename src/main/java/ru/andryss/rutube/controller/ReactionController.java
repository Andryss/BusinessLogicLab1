package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andryss.rutube.exception.RequestValidationException;
import ru.andryss.rutube.interactor.ReactionInteractor;
import ru.andryss.rutube.message.CreateReactionRequest;

@Validated
@RestController
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionInteractor interactor;

    @PostMapping("/api/reactions")
    public ResponseEntity<?> postApiReactions(
            @RequestBody @Valid CreateReactionRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal User user
            ) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        interactor.postApiReactions(request, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/reactions")
    public ResponseEntity<?> getApiReactions(
            @RequestParam @NotBlank @UUID String sourceId
    ) {
        return ResponseEntity.ok(interactor.getApiReactions(sourceId));
    }

    @GetMapping("/api/reactions/my")
    public ResponseEntity<?> getApiReactionsMy(
            @RequestParam @NotBlank @UUID String sourceId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(interactor.getApiReactionsMy(sourceId, user));
    }
}
