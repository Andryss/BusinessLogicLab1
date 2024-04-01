package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.ModerationResendInfo;
import ru.andryss.rutube.message.ModerationResultInfo;
import ru.andryss.rutube.service.ModerationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModerationInteractorImpl implements ModerationInteractor {

    private final ModerationService moderationService;

    @Override
    public void handleModerationResult(ModerationResultInfo result) {
        log.info("got ModerationResult message {}", result);
        moderationService.handleResult(result.getSourceId(), result.getStatus(), result.getComment(), result.getCreatedAt());
    }

    @Override
    public void handleModerationResend(ModerationResendInfo resend) {
        log.info("got ModerationResend message {}", resend);
        moderationService.handleResend(resend.getSourceId());
    }
}
