package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.andryss.rutube.interactor.ModerationInteractor;
import ru.andryss.rutube.message.UploadModerationResultRequest;

@Validated
@RestController
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationInteractor interactor;

    @GetMapping("/api/moderation/next")
    public ResponseEntity<?> getApiModerationNext(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(interactor.getApiModerationNext(user));
    }

    @PostMapping("/api/moderation")
    public ResponseEntity<?> postApiModeration(
            @RequestBody @Valid UploadModerationResultRequest request,
            @AuthenticationPrincipal User user
    ) {
        interactor.postApiModeration(request, user);
        return ResponseEntity.noContent().build();
    }

}
