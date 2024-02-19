package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.GetStatusResponse;
import ru.andryss.rutube.message.NewVideoResponse;
import ru.andryss.rutube.message.PutVideoRequest;
import ru.andryss.rutube.service.SourceService;
import ru.andryss.rutube.service.VideoService;
import ru.andryss.rutube.service.VideoService.VideoInfo;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VideoInteractorImpl implements VideoInteractor {

    private final SourceService sourceService;
    private final VideoService videoService;

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
    public void putApiVideos(String sourceId, PutVideoRequest request, User user) {
        VideoInfo videoInfo = new VideoInfo(request.getTitle(), request.getDescription(), request.getCategory(),
                request.getAccess(), request.isAgeRestriction(), request.isComments());
        videoService.putVideo(sourceId, user.getUsername(), videoInfo);
    }

    @Override
    public GetStatusResponse getApiVideosStatus(String sourceId, User user) {
        GetStatusResponse response = new GetStatusResponse();
        response.setStatus(videoService.getVideoStatus(sourceId, user.getUsername()));
        return response;
    }

    @Override
    public void postApiVideosPublish(String sourceId, User user) {
        videoService.publishVideo(sourceId, user.getUsername());
    }
}
