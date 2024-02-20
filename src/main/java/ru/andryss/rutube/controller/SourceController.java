package ru.andryss.rutube.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.andryss.rutube.service.SourceService;

@RestController
@RequiredArgsConstructor
public class SourceController {

    private final SourceService sourceService;

    @PutMapping("/api/sources/{sourceId}")
    public ResponseEntity<?> putApiSources(
            @PathVariable String sourceId,
            @RequestParam MultipartFile file
    ) {
        sourceService.putVideo(sourceId, file);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/sources/{sourceId}")
    public ResponseEntity<?> getApiSources(
            @PathVariable String sourceId
    ) {
        return ResponseEntity.ok(sourceService.getVideo(sourceId));
    }
}
