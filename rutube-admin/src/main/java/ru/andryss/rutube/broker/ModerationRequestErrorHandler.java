package ru.andryss.rutube.broker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.ModerationRequestInfo;
import ru.andryss.rutube.service.ModerationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModerationRequestErrorHandler implements ConsumerAwareListenerErrorHandler {

    private final ModerationService moderationService;

    @SuppressWarnings("NullableProblems")
    // @SendTo("${topic.moderation.resends}") // sadness :(
    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        log.error("handle exception during moderation request handling", exception);

        ModerationRequestInfo request = (ModerationRequestInfo) message.getPayload();
        moderationService.requestResend(request.getSourceId());

        log.info("resending requested for source {}", request.getSourceId());

        return null;
    }
}
