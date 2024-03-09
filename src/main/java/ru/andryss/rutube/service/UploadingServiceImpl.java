package ru.andryss.rutube.service;

import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.exception.IncorrectVideoStatusException;
import ru.andryss.rutube.exception.VideoNotFoundException;
import ru.andryss.rutube.model.ModerationRequest;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.repository.ModerationRequestRepository;
import ru.andryss.rutube.repository.VideoRepository;

import java.time.Instant;

import static ru.andryss.rutube.model.VideoStatus.MODERATION_PENDING;
import static ru.andryss.rutube.model.VideoStatus.UPLOAD_PENDING;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadingServiceImpl implements UploadingService {

    @Value("${storage.bucket-name}")
    private String bucketName;

    @Value("${storage.url.expiration-time-ms}")
    private int linkExpirationMs;

    @Value("${storage.wait.delay-ms}")
    private int waitDelayMs;

    private final VideoRepository videoRepository;
    private final ModerationRequestRepository requestRepository;
    private final TransactionTemplate transactionTemplate;
    private final MinioClient minioClient;

    @Override
    @Async("uploadWaitingExecutor")
    public void validateUploading(String sourceId) {
        log.info("Wait uploading of source {}", sourceId);
        long start = System.nanoTime();

        int maxTries = linkExpirationMs / waitDelayMs;
        StatObjectResponse objectStat = tryWaitObject(sourceId, maxTries, waitDelayMs);

        long waitTimeMillis = (System.nanoTime() - start) / 1_000_000;

        if (objectStat == null) {
            log.warn("Source {} wasn't uploaded within {}ms", sourceId, waitTimeMillis);
            return;
        } else {
            log.info("Source {} successfully uploaded within {}ms", sourceId, waitTimeMillis);
        }

        transactionTemplate.executeWithoutResult(status -> {
            Video video = videoRepository.findById(sourceId).orElseThrow(() -> new VideoNotFoundException(sourceId));
            if (video.getStatus() != UPLOAD_PENDING) {
                throw new IncorrectVideoStatusException(video.getStatus(), UPLOAD_PENDING);
            }

            video.setStatus(MODERATION_PENDING);

            ModerationRequest request = new ModerationRequest();
            request.setSourceId(sourceId);
            request.setCreatedAt(Instant.now());

            videoRepository.save(video);
            requestRepository.save(request);
        });
    }

    @Nullable
    private StatObjectResponse tryWaitObject(String object, int maxTries, int delayMillis) {
        int currentTries = 0;
        while (currentTries <= maxTries) {
            StatObjectResponse response = checkObjectExist(object);
            if (response != null) {
                return response;
            }
            currentTries++;
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                return null;
            }
        }
        return null;
    }

    @Nullable
    private StatObjectResponse checkObjectExist(String object) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(object)
                            .build());
        } catch (ErrorResponseException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
