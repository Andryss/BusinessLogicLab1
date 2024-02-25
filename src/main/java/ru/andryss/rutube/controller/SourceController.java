package ru.andryss.rutube.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.andryss.rutube.service.SourceService;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class SourceController {

    private final SourceService sourceService;

    @PutMapping(value = "/api/sources/{sourceId}", consumes = "multipart/form-data")
    @ResponseStatus(NO_CONTENT)
    public void putApiSources(
            @PathVariable String sourceId,
            @RequestParam MultipartFile file
    ) {
        sourceService.putVideo(sourceId, file);
    }

    @GetMapping(value = "/api/sources/{sourceId}", produces = "video/mp4")
    @ResponseStatus(OK)
    public byte[] getApiSources(
            @PathVariable String sourceId
    ) {
        return sourceService.getVideo(sourceId);
    }
}
