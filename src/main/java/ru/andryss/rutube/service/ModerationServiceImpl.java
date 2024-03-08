package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.exception.IncorrectVideoStatusException;
import ru.andryss.rutube.exception.SourceNotFoundException;
import ru.andryss.rutube.exception.VideoNotFoundException;
import ru.andryss.rutube.model.*;
import ru.andryss.rutube.repository.ModerationRequestRepository;
import ru.andryss.rutube.repository.ModerationResultRepository;
import ru.andryss.rutube.repository.VideoRepository;

import java.time.Instant;
import java.util.Optional;

import static ru.andryss.rutube.model.ModerationStatus.SUCCESS;
import static ru.andryss.rutube.model.VideoStatus.*;

@Service
@RequiredArgsConstructor
public class ModerationServiceImpl implements ModerationService {

    private final ModerationRequestRepository requestRepository;
    private final ModerationResultRepository resultRepository;
    private final VideoRepository videoRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public Optional<String> getNextModeration(String username) {
        return transactionTemplate.execute(status -> {
            Optional<String> alreadyAssigned = requestRepository.findByAssignee(username).map(ModerationRequest::getSourceId);
            if (alreadyAssigned.isPresent()) {
                return alreadyAssigned;
            }
            requestRepository.assignModeration(username, Instant.now());
            return requestRepository.findByAssignee(username).map(ModerationRequest::getSourceId);
        });
    }

    @Override
    public void uploadModeration(String sourceId, String username, ModerationStatus status, String comment) {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            ModerationRequest request = requestRepository.findByAssignee(username).orElseThrow(() -> new SourceNotFoundException(sourceId));

            if (!request.getSourceId().equals(sourceId)) {
                throw new SourceNotFoundException(sourceId);
            }

            Video video = videoRepository.findById(sourceId).orElseThrow(() -> new VideoNotFoundException(sourceId));
            if (video.getStatus() != MODERATION_PENDING) {
                throw new IncorrectVideoStatusException(video.getStatus(), MODERATION_PENDING);
            }

            ModerationResult result = new ModerationResult();
            result.setSourceId(sourceId);
            result.setAssignee(username);
            result.setStatus(status);
            result.setComment(comment);
            result.setCreatedAt(Instant.now());

            if (status != SUCCESS) {
                video.setStatus(MODERATION_FAILED);
            } else if (video.getTitle() != null && video.getDescription() != null && video.getCategory() != null && video.getAccess() != null) {
                video.setStatus(READY);
            } else {
                video.setStatus(FILL_PENDING);
            }

            resultRepository.save(result);
            videoRepository.save(video);
            requestRepository.delete(request);
        });
    }

    @Override
    public String getModerationComment(String sourceId) {
        ModerationResult result = resultRepository.findById(sourceId).orElseThrow(() -> new SourceNotFoundException(sourceId));
        return result.getComment();
    }
}
