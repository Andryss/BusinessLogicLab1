package ru.andryss.rutube.interactor;

import ru.andryss.rutube.message.ModerationResultInfo;

public interface ModerationInteractor {
    /**
     * Handles message from moderation.results topic
     */
    void handleModerationResult(ModerationResultInfo result);
}
