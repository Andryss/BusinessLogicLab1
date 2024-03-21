package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.GetNextModerationResponse;
import ru.andryss.rutube.message.ModerationRequestInfo;
import ru.andryss.rutube.message.UploadModerationResultRequest;
import ru.andryss.rutube.security.CustomUserDetails;
import ru.andryss.rutube.service.ModerationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModerationInteractorImpl implements ModerationInteractor {

    private final ModerationService moderationService;

    @Override
    public GetNextModerationResponse getApiModerationNext(CustomUserDetails user) {
        GetNextModerationResponse response = new GetNextModerationResponse();

        moderationService.getNextModeration(user.getUsername()).ifPresent(info -> {
            response.setSourceId(info.getSourceId());
            response.setDownloadLink(info.getDownloadLink());
        });

        return response;
    }

    @Override
    public void postApiModeration(UploadModerationResultRequest request, CustomUserDetails user) {
        moderationService.uploadModeration(request.getSourceId(), user.getUsername(), request.getResult(), request.getComment());
    }

    @Override
    public void moderationRequestMessage(ModerationRequestInfo request) {
        log.info("got ModerationRequest message {}", request);
        moderationService.handleRequest(request.getSourceId(), request.getDownloadLink(), request.getCreatedAt());
    }
}
