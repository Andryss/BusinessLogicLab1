package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.andryss.rutube.exception.*;
import ru.andryss.rutube.model.ModerationRequest;
import ru.andryss.rutube.model.Source;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.repository.ModerationRequestRepository;
import ru.andryss.rutube.repository.SourceRepository;
import ru.andryss.rutube.repository.VideoRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static ru.andryss.rutube.model.VideoStatus.MODERATION_PENDING;
import static ru.andryss.rutube.model.VideoStatus.UPLOAD_PENDING;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;
    private final VideoRepository videoRepository;
    private final ModerationRequestRepository requestRepository;

    private final Set<String> uploadLinks = new HashSet<>();
    private final Set<String> downloadLinks = new HashSet<>();

    @Override
    public String generateUploadLink(String sourceId) {
        uploadLinks.add(sourceId);
        return String.format("/api/source/%s", sourceId);
    }

    @Override
    public void putVideo(String sourceId, MultipartFile file) {
        if (!uploadLinks.contains(sourceId)) {
            throw new LinkNotFountException(sourceId);
        }

        if (file.getContentType() == null) {
            throw new IllegalVideoException();
        }
        if (!file.getContentType().startsWith("video/")) {
            throw new IllegalVideoException();
        }
        if (file.getSize() <= 0 || file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalVideoException();
        }

        byte[] content;
        try {
            content = file.getBytes();
        } catch (IOException e) {
            throw new IllegalVideoException();
        }

        Video video = videoRepository.findById(sourceId).orElseThrow(() -> new VideoNotFoundException(sourceId));
        if (video.getStatus() != UPLOAD_PENDING) {
            throw new IncorrectVideoStatusException(video.getStatus(), UPLOAD_PENDING);
        }

        video.setStatus(MODERATION_PENDING);

        Source source = new Source();
        source.setSourceId(sourceId);
        source.setContent(content);

        ModerationRequest request = new ModerationRequest();
        request.setSourceId(sourceId);
        request.setCreatedAt(Instant.now());

        sourceRepository.save(source);
        uploadLinks.remove(sourceId);
        videoRepository.save(video);
        requestRepository.save(request);
    }

    @Override
    public String generateDownloadLink(String sourceId) {
        downloadLinks.add(sourceId);
        return String.format("/api/source/%s", sourceId);
    }

    @Override
    public byte[] getVideo(String sourceId) {
        if (!downloadLinks.contains(sourceId)) {
            throw new LinkNotFountException(sourceId);
        }

        Source source = sourceRepository.findById(sourceId).orElseThrow(() -> new SourceNotFoundException(sourceId));

        return source.getContent();
    }
}
