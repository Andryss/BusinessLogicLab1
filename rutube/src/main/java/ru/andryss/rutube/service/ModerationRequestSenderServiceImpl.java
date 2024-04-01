package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.message.ModerationRequestInfo;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModerationRequestSenderServiceImpl implements ModerationRequestSenderService {

    private final KafkaProducer<String, Object> moderationRequestProducer;

    @Value("${topic.moderation.requests}")
    private String moderationRequestsTopic;

    @Value("${topic.resend.count}")
    private int maxResendCount;

    private final Map<String, AtomicInteger> resendCounts = new ConcurrentHashMap<>();

    @Override
    public void sendFirstTime(String sourceId, String downloadLink) {
        ModerationRequestInfo requestInfo = new ModerationRequestInfo();
        requestInfo.setSourceId(sourceId);
        requestInfo.setDownloadLink(downloadLink);
        requestInfo.setCreatedAt(Instant.now());

        moderationRequestProducer.send(new ProducerRecord<>(moderationRequestsTopic, requestInfo));
    }

    @Override
    public void resend(String sourceId, String downloadLink) {
        AtomicInteger resendCount = resendCounts.computeIfAbsent(sourceId, s -> new AtomicInteger(0));
        if (resendCount.get() >= maxResendCount) {
            log.error("Resending moderation requests for source {} limit exceeded ({} out of {})", sourceId, resendCount, maxResendCount);
            clearResendInfo(sourceId);
            return;
        }
        log.warn("Resending moderation request for source {} ({} out of {})", sourceId, resendCount.incrementAndGet(), maxResendCount);
        sendFirstTime(sourceId, downloadLink);
    }

    @Override
    public void clearResendInfo(String sourceId) {
        resendCounts.remove(sourceId);
        log.info("Resend info for source {} cleared", sourceId);
    }
}
