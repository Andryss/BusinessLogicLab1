package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andryss.rutube.interactor.CommentInteractor;
import ru.andryss.rutube.message.CreateCommentRequest;

@Validated
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentInteractor commentInteractor;

    @PostMapping("/api/comments")
    public ResponseEntity<?> postApiComments(
            @RequestBody @Valid CreateCommentRequest request,
            @AuthenticationPrincipal User user
    ) {
        commentInteractor.postApiComments(request, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/comments")
    public ResponseEntity<?> getApiComments(
            @RequestParam @NotBlank @UUID String sourceId
    ) {
        return ResponseEntity.ok(commentInteractor.getApiComments(sourceId));
    }

}
