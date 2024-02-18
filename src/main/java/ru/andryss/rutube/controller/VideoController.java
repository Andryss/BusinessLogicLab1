package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> postApiVideosNew() {
        return ResponseEntity.ok(interactor.postApiVideosNew());
    }

    @PutMapping("/api/videos/{sourceId}")
    public ResponseEntity<?> putApiVideos(
            @PathVariable @UUID String sourceId,
            @RequestBody @Valid PutVideoRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        interactor.putApiVideos(sourceId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/videos/{sourceId}/status")
    public ResponseEntity<?> getApiVideosStatus(
            @PathVariable @UUID String sourceId
    ) {
        return ResponseEntity.ok(interactor.getApiVideosStatus(sourceId));
    }

    @PostMapping("/api/videos/{sourceId}:publish")
    public ResponseEntity<?> postApiVideosPublish(
            @PathVariable @UUID String sourceId
    ) {
        interactor.postApiVideosPublish(sourceId);
        return ResponseEntity.noContent().build();
    }
}
