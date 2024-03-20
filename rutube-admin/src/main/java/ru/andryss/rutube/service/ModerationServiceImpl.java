package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.andryss.rutube.exception.SourceNotFoundException;
import ru.andryss.rutube.message.ModerationInfo;
import ru.andryss.rutube.message.ModerationResultInfo;
import ru.andryss.rutube.model.ModerationHistory;
import ru.andryss.rutube.model.ModerationRequest;
import ru.andryss.rutube.model.ModerationStatus;
import ru.andryss.rutube.repository.ModerationHistoryRepository;
import ru.andryss.rutube.repository.ModerationRequestRepository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModerationServiceImpl implements ModerationService {

    private final ModerationRequestRepository requestRepository;
    private final ModerationHistoryRepository historyRepository;
    private final KafkaProducer<String, ModerationResultInfo> moderationResultProducer;

    @Value("${topic.moderation.results}")
    private String moderationResultsTopic;

    @Override
    @Retryable(retryFor = SQLException.class)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Optional<ModerationInfo> getNextModeration(String username) {
        Optional<ModerationRequest> alreadyAssigned = requestRepository.findByAssignee(username);
        if (alreadyAssigned.isPresent()) {
            ModerationRequest request = alreadyAssigned.get();
            return Optional.of(new ModerationInfo(request.getSourceId(), request.getDownloadLink()));
        }
        requestRepository.assignModeration(username, Instant.now());
        return requestRepository.findByAssignee(username).map(request -> new ModerationInfo(request.getSourceId(), request.getDownloadLink()));
    }

    @Override
    @Retryable(retryFor = SQLException.class)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void uploadModeration(String sourceId, String username, ModerationStatus status, String comment) {
        ModerationRequest request = requestRepository.findByAssignee(username).orElseThrow(() -> new SourceNotFoundException(sourceId));

        if (!request.getSourceId().equals(sourceId)) {
            throw new SourceNotFoundException(sourceId);
        }

        Instant now = Instant.now();

        ModerationHistory history = new ModerationHistory();
        history.setSourceId(sourceId);
        history.setAssignee(username);
        history.setStatus(status);
        history.setComment(comment);
        history.setCreatedAt(now);

        ModerationResultInfo resultInfo = new ModerationResultInfo();
        resultInfo.setSourceId(sourceId);
        resultInfo.setStatus(status);
        resultInfo.setComment(comment);
        history.setCreatedAt(now);

        historyRepository.save(history);
        requestRepository.delete(request);
        moderationResultProducer.send(new ProducerRecord<>(moderationResultsTopic, resultInfo));
    }

    @Override
    @Retryable(retryFor = SQLException.class)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handleRequest(String sourceId, String downloadLink, Instant createdAt) {
        ModerationRequest request = new ModerationRequest();
        request.setSourceId(sourceId);
        request.setDownloadLink(downloadLink);
        request.setCreatedAt(createdAt);

        requestRepository.save(request);
    }
}
