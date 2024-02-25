package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andryss.rutube.interactor.VideoInteractor;
import ru.andryss.rutube.message.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoInteractor interactor;

    @PostMapping("/api/videos:new")
    @ResponseStatus(OK)
    public NewVideoResponse postApiVideosNew(
            @RequestParam(required = false) String prototype,
            @AuthenticationPrincipal User user
    ) {
        return interactor.postApiVideosNew(prototype, user);
    }

    @GetMapping("/api/videos")
    @ResponseStatus(OK)
    public GetVideosResponse getApiVideos(
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNumber,
            @RequestParam(defaultValue = "1") @Positive int pageSize
    ) {
        return interactor.getApiVideos(PageRequest.of(pageNumber, pageSize));
    }

    @GetMapping("/api/videos/{sourceId}")
    @ResponseStatus(OK)
    public GetVideoResponse getApiVideo(
            @PathVariable String sourceId
    ) {
        return interactor.getApiVideo(sourceId);
    }

    @PutMapping("/api/videos/{sourceId}")
    @ResponseStatus(NO_CONTENT)
    public void putApiVideos(
            @PathVariable String sourceId,
            @RequestBody @Valid PutVideoRequest request,
            @AuthenticationPrincipal User user
    ) {
        interactor.putApiVideos(sourceId, request, user);
    }

    @GetMapping("/api/videos/{sourceId}/status")
    @ResponseStatus(OK)
    public GetVideoStatusResponse getApiVideosStatus(
            @PathVariable @NotBlank String sourceId,
            @AuthenticationPrincipal User user
    ) {
        return interactor.getApiVideosStatus(sourceId, user);
    }

    @PostMapping("/api/videos/{sourceId}:publish")
    @ResponseStatus(NO_CONTENT)
    public void postApiVideosPublish(
            @PathVariable @NotBlank String sourceId,
            @AuthenticationPrincipal User user
    ) {
        interactor.postApiVideosPublish(sourceId, user);
    }
}
