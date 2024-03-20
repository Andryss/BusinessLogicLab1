package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.ModerationResultInfo;
import ru.andryss.rutube.service.ModerationService;

@Component
@RequiredArgsConstructor
public class ModerationInteractorImpl implements ModerationInteractor {

    private final ModerationService moderationService;

    @Override
    public void handleModerationResult(ModerationResultInfo result) {
        moderationService.handleResult(result.getSourceId(), result.getStatus(), result.getComment(), result.getCreatedAt());
    }
}
