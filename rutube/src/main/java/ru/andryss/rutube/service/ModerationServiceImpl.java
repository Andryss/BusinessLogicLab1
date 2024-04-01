package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.exception.IncorrectVideoStatusException;
import ru.andryss.rutube.exception.SourceNotFoundException;
import ru.andryss.rutube.exception.VideoNotFoundException;
import ru.andryss.rutube.model.ModerationResult;
import ru.andryss.rutube.model.ModerationStatus;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.repository.ModerationResultRepository;
import ru.andryss.rutube.repository.VideoRepository;

import java.time.Instant;

import static ru.andryss.rutube.model.ModerationStatus.SUCCESS;
import static ru.andryss.rutube.model.VideoStatus.*;

@Service
@RequiredArgsConstructor
public class ModerationServiceImpl implements ModerationService {

    private final ModerationResultRepository resultRepository;
    private final VideoRepository videoRepository;
    private final SourceService sourceService;
    private final ModerationRequestSenderService requestSenderService;
    private final TransactionTemplate transactionTemplate;
    private final TransactionTemplate readOnlyTransactionTemplate;

    @Override
    public void handleResult(String sourceId, ModerationStatus status, String comment, Instant createdAt) {
        transactionTemplate.executeWithoutResult(s -> {
            if (resultRepository.existsById(sourceId)) {
                return;
            }

            Video video = videoRepository.findById(sourceId).orElseThrow(() -> new VideoNotFoundException(sourceId));
            if (video.getStatus() != MODERATION_PENDING) {
                throw new IncorrectVideoStatusException(video.getStatus(), MODERATION_PENDING);
            }

            ModerationResult result = new ModerationResult();
            result.setSourceId(sourceId);
            result.setStatus(status);
            result.setComment(comment);
            result.setCreatedAt(createdAt);

            if (status != SUCCESS) {
                video.setStatus(MODERATION_FAILED);
            } else if (video.getTitle() != null && video.getDescription() != null && video.getCategory() != null && video.getAccess() != null) {
                video.setStatus(READY);
            } else {
                video.setStatus(FILL_PENDING);
            }
            video.setUpdatedAt(Instant.now());

            resultRepository.save(result);
            videoRepository.save(video);
        });
    }

    @Override
    public void handleResend(String sourceId) {
        requestSenderService.resend(sourceId, sourceService.generateDownloadLink(sourceId));
    }

    @Override
    public String getModerationComment(String sourceId) {
        return readOnlyTransactionTemplate.execute(status -> {
            ModerationResult result = resultRepository.findById(sourceId).orElseThrow(() -> new SourceNotFoundException(sourceId));

            return result.getComment();
        });
    }
}
