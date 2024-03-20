package ru.andryss.rutube.broker;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.interactor.ModerationInteractor;
import ru.andryss.rutube.message.ModerationRequestInfo;

@Component
@RequiredArgsConstructor
public class ModerationMessageHandler {

    private final ModerationInteractor interactor;

    @KafkaListener(topics = "${topic.moderation.requests}", groupId = "default")
    public void handleModerationRequest(
            @Payload ModerationRequestInfo info
    ) {
        interactor.moderationRequestMessage(info);
    }

}
