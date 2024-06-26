package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.andryss.rutube.exception.*;
import ru.andryss.rutube.model.Source;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.repository.SourceRepository;
import ru.andryss.rutube.repository.VideoRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static ru.andryss.rutube.model.VideoStatus.MODERATION_PENDING;
import static ru.andryss.rutube.model.VideoStatus.UPLOAD_PENDING;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;
    private final VideoRepository videoRepository;
    private final ModerationRequestSenderService requestSenderService;
    private final TransactionTemplate transactionTemplate;
    private final TransactionTemplate readOnlyTransactionTemplate;

    private final Set<String> uploadLinks = new ConcurrentSkipListSet<>();
    private final Set<String> downloadLinks = new ConcurrentSkipListSet<>();

    @Override
    public String generateUploadLink(String sourceId) {
        uploadLinks.add(sourceId);
        return String.format("/api/source/%s", sourceId);
    }

    @Override
    @Retryable(retryFor = SQLException.class)
    public void putVideo(String sourceId, MultipartFile file) {
        if (!uploadLinks.contains(sourceId)) {
            throw new LinkNotFoundException(sourceId);
        }

        if (file.getContentType() == null || !file.getContentType().equals("video/mp4") ||
                file.getSize() <= 0 || file.getSize() > 2 * 1024 * 1024) {
            throw new IllegalVideoFormatException();
        }

        byte[] content;
        try {
            content = file.getBytes();
        } catch (IOException e) {
            throw new IllegalVideoFormatException();
        }

        transactionTemplate.executeWithoutResult(status -> {
            Video video = videoRepository.findById(sourceId).orElseThrow(() -> new VideoNotFoundException(sourceId));
            if (video.getStatus() != UPLOAD_PENDING) {
                throw new IncorrectVideoStatusException(video.getStatus(), UPLOAD_PENDING);
            }

            video.setStatus(MODERATION_PENDING);

            Source source = new Source();
            source.setSourceId(sourceId);
            source.setContent(content);

            sourceRepository.save(source);
            uploadLinks.remove(sourceId);
            videoRepository.save(video);
        });

        requestSenderService.sendFirstTime(sourceId, generateDownloadLink(sourceId));
    }

    @Override
    public String generateDownloadLink(String sourceId) {
        downloadLinks.add(sourceId);
        return String.format("/api/source/%s", sourceId);
    }

    @Override
    public byte[] getVideo(String sourceId) {
        if (!downloadLinks.contains(sourceId)) {
            throw new LinkNotFoundException(sourceId);
        }
        return readOnlyTransactionTemplate.execute(status -> {
            Source source = sourceRepository.findById(sourceId).orElseThrow(() -> new SourceNotFoundException(sourceId));

            return source.getContent();
        });
    }
}
