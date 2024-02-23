package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.GetVideoStatusResponse;
import ru.andryss.rutube.message.GetVideosResponse;
import ru.andryss.rutube.message.NewVideoResponse;
import ru.andryss.rutube.message.PutVideoRequest;
import ru.andryss.rutube.model.VideoStatus;
import ru.andryss.rutube.service.ModerationService;
import ru.andryss.rutube.service.SourceService;
import ru.andryss.rutube.service.VideoService;
import ru.andryss.rutube.service.VideoService.VideoChangeInfo;

import java.util.UUID;

import static ru.andryss.rutube.model.VideoStatus.MODERATION_FAILED;

@Component
@RequiredArgsConstructor
public class VideoInteractorImpl implements VideoInteractor {

    private final SourceService sourceService;
    private final VideoService videoService;
    private final ModerationService moderationService;

    @Override
    public NewVideoResponse postApiVideosNew(String prototype, User user) {
        String sourceId = UUID.randomUUID().toString();
        String uploadLink = sourceService.generateUploadLink(sourceId);
        videoService.createNewVideo(sourceId, user.getUsername(), prototype);

        NewVideoResponse response = new NewVideoResponse();
        response.setSourceId(sourceId);
        response.setUploadLink(uploadLink);
        return response;
    }

    @Override
    public GetVideosResponse getApiVideos() {
        GetVideosResponse response = new GetVideosResponse();
        response.setVideos(videoService.getPublishedVideos());
        return response;
    }

    @Override
    public void putApiVideos(String sourceId, PutVideoRequest request, User user) {
        VideoChangeInfo videoChangeInfo = new VideoChangeInfo(request.getTitle(), request.getDescription(), request.getCategory(),
                request.getAccess(), request.isAgeRestriction(), request.isComments());
        videoService.putVideo(sourceId, user.getUsername(), videoChangeInfo);
    }

    @Override
    public GetVideoStatusResponse getApiVideosStatus(String sourceId, User user) {
        VideoStatus status = videoService.getVideoStatus(sourceId, user.getUsername());

        GetVideoStatusResponse response = new GetVideoStatusResponse();
        response.setStatus(status);
        if (status == MODERATION_FAILED) {
            response.setComment(moderationService.getModerationComment(sourceId));
        }
        return response;
    }

    @Override
    public void postApiVideosPublish(String sourceId, User user) {
        videoService.publishVideo(sourceId, user.getUsername());
    }
}
