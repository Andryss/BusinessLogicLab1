package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andryss.rutube.exception.RequestValidationException;
import ru.andryss.rutube.interactor.VideoInteractor;
import ru.andryss.rutube.message.PutVideoRequest;

@Validated
@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoInteractor interactor;

    @PostMapping("/api/videos:new")
    public ResponseEntity<?> postApiVideosNew(
            @RequestParam(required = false) @UUID String prototype,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(interactor.postApiVideosNew(prototype, user));
    }

    @PutMapping("/api/videos/{sourceId}")
    public ResponseEntity<?> putApiVideos(
            @PathVariable @UUID String sourceId,
            @RequestBody @Valid PutVideoRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal User user
    ) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        interactor.putApiVideos(sourceId, request, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/videos/{sourceId}/status")
    public ResponseEntity<?> getApiVideosStatus(
            @PathVariable @UUID String sourceId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(interactor.getApiVideosStatus(sourceId, user));
    }

    @PostMapping("/api/videos/{sourceId}:publish")
    public ResponseEntity<?> postApiVideosPublish(
            @PathVariable @UUID String sourceId,
            @AuthenticationPrincipal User user
    ) {
        interactor.postApiVideosPublish(sourceId, user);
        return ResponseEntity.noContent().build();
    }
}
