package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andryss.rutube.interactor.VideoInteractor;
import ru.andryss.rutube.message.*;
import ru.andryss.rutube.security.CustomUserDetails;

@Validated
@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoInteractor interactor;

    @PostMapping("/api/videos:new")
    public NewVideoResponse postApiVideosNew(
            @RequestParam(required = false) String prototype,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return interactor.postApiVideosNew(prototype, user);
    }

    @GetMapping("/api/videos")
    public GetVideosResponse getApiVideos(
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNumber,
            @RequestParam(defaultValue = "1") @Positive @Max(100) int pageSize
    ) {
        return interactor.getApiVideos(PageRequest.of(pageNumber, pageSize));
    }

    @GetMapping("/api/videos/{sourceId}")
    public GetVideoResponse getApiVideo(
            @PathVariable String sourceId
    ) {
        return interactor.getApiVideo(sourceId);
    }

    @PutMapping("/api/videos/{sourceId}")
    public void putApiVideos(
            @PathVariable String sourceId,
            @RequestBody @Valid PutVideoRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        interactor.putApiVideos(sourceId, request, user);
    }

    @GetMapping("/api/videos/{sourceId}/status")
    public GetVideoStatusResponse getApiVideosStatus(
            @PathVariable @NotBlank String sourceId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return interactor.getApiVideosStatus(sourceId, user);
    }

    @PostMapping("/api/videos/{sourceId}:publish")
    public void postApiVideosPublish(
            @PathVariable @NotBlank String sourceId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        interactor.postApiVideosPublish(sourceId, user);
    }
}
