package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.GetNextModerationResponse;
import ru.andryss.rutube.message.UploadModerationResultRequest;
import ru.andryss.rutube.service.ModerationService;
import ru.andryss.rutube.service.SourceService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ModerationInteractorImpl implements ModerationInteractor {

    private final ModerationService moderationService;
    private final SourceService sourceService;

    @Override
    public GetNextModerationResponse getApiModerationNext(User user) {
        Optional<String> sourceIdOptional = moderationService.getNextModeration(user.getUsername());
        if (sourceIdOptional.isEmpty()) {
            return new GetNextModerationResponse();
        }

        String sourceId = sourceIdOptional.get();
        String downloadLink = sourceService.generateDownloadLink(sourceId);

        GetNextModerationResponse response = new GetNextModerationResponse();
        response.setSourceId(sourceId);
        response.setDownloadLink(downloadLink);
        return response;
    }

    @Override
    public void postApiModeration(UploadModerationResultRequest request, User user) {
        moderationService.uploadModeration(request.getSourceId(), user.getUsername(), request.getResult(), request.getComment());
    }
}
