package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
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
    public NewVideoResponse postApiVideosNew(String prototype) {
        // TODO: determine user
        String username = "username";

        String sourceId = UUID.randomUUID().toString();
        String uploadLink = sourceService.generateUploadLink(sourceId);
        videoService.createNewVideo(sourceId, username, prototype);

        NewVideoResponse response = new NewVideoResponse();
        response.setSourceId(sourceId);
        response.setUploadLink(uploadLink);
        return response;
    }

    @Override
    public void putApiVideos(String sourceId, PutVideoRequest request) {
        // TODO: determine user
        String username = "username";

        VideoInfo videoInfo = new VideoInfo(request.getTitle(), request.getDescription(), request.getCategory(),
                request.getAccess(), request.isAgeRestriction(), request.isComments());

        videoService.putVideo(sourceId, username, videoInfo);
    }

    @Override
    public GetStatusResponse getApiVideosStatus(String sourceId) {
        // TODO: determine user
        String username = "username";

        GetStatusResponse response = new GetStatusResponse();
        response.setStatus(videoService.getVideoStatus(sourceId, username));
        return response;
    }

    @Override
    public void postApiVideosPublish(String sourceId) {
        // TODO: determine user
        String username = "username";

        videoService.publishVideo(sourceId, username);
    }
}
