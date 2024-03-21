package ru.andryss.rutube.broker;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.interactor.ModerationInteractor;
import ru.andryss.rutube.message.ModerationResultInfo;

@Component
@RequiredArgsConstructor
public class ModerationMessageHandler {

    private final ModerationInteractor interactor;

    @KafkaListener(topics = "${topic.moderation.results}", groupId = "default", containerFactory = "resultListenerFactory")
    public void handleModerationResult(
            @Payload ModerationResultInfo info
    ) {
        interactor.handleModerationResult(info);
    }

}
