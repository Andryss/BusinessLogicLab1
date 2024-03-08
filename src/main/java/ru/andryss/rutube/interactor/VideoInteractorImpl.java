package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.*;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.model.VideoStatus;
import ru.andryss.rutube.security.CustomUserDetails;
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
    public NewVideoResponse postApiVideosNew(String prototype, CustomUserDetails user) {
        String sourceId = UUID.randomUUID().toString();
        String uploadLink = sourceService.generateUploadLink(sourceId);
        videoService.createNewVideo(sourceId, user.getUsername(), prototype);

        NewVideoResponse response = new NewVideoResponse();
        response.setSourceId(sourceId);
        response.setUploadLink(uploadLink);
        return response;
    }

    @Override
    public GetVideosResponse getApiVideos(PageRequest pageRequest) {
        GetVideosResponse response = new GetVideosResponse();
        response.setVideos(videoService.getPublishedVideos(pageRequest));
        return response;
    }

    @Override
    public GetVideoResponse getApiVideo(String sourceId) {
        Video video = videoService.findPublishedVideo(sourceId);
        String downloadLink = sourceService.generateDownloadLink(sourceId);

        GetVideoResponse response = new GetVideoResponse();
        response.setDownloadLink(downloadLink);
        response.setAuthor(video.getAuthor());
        response.setTitle(video.getTitle());
        response.setDescription(video.getDescription());
        response.setCategory(video.getCategory());
        response.setAgeRestriction(video.isAgeRestriction());
        response.setComments(video.isComments());
        response.setPublishedAt(video.getPublishedAt());

        return response;
    }

    @Override
    public void putApiVideos(String sourceId, PutVideoRequest request, CustomUserDetails user) {
        VideoChangeInfo videoChangeInfo = new VideoChangeInfo(request.getTitle().trim(), request.getDescription().trim(),
                request.getCategory(), request.getAccess(), request.isAgeRestriction(), request.isComments());
        videoService.putVideo(sourceId, user.getUsername(), videoChangeInfo);
    }

    @Override
    public GetVideoStatusResponse getApiVideosStatus(String sourceId, CustomUserDetails user) {
        VideoStatus status = videoService.getVideoStatus(sourceId, user.getUsername());

        GetVideoStatusResponse response = new GetVideoStatusResponse();
        response.setStatus(status);
        if (status == MODERATION_FAILED) {
            response.setComment(moderationService.getModerationComment(sourceId));
        }
        return response;
    }

    @Override
    public void postApiVideosPublish(String sourceId, CustomUserDetails user) {
        videoService.publishVideo(sourceId, user.getUsername());
    }
}
