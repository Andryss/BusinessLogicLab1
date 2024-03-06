package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.exception.VideoAlreadyPublishedException;
import ru.andryss.rutube.exception.VideoNotFoundException;
import ru.andryss.rutube.exception.IncorrectVideoStatusException;
import ru.andryss.rutube.message.VideoThumbInfo;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.model.VideoStatus;
import ru.andryss.rutube.repository.VideoRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static ru.andryss.rutube.model.VideoAccess.PUBLIC;
import static ru.andryss.rutube.model.VideoStatus.*;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void createNewVideo(String sourceId, String author, String prototype) {

        Video video = new Video();
        Instant now = Instant.now();

        video.setSourceId(sourceId);
        video.setAuthor(author);
        video.setStatus(UPLOAD_PENDING);
        video.setCreatedAt(now);
        video.setUpdatedAt(now);

        transactionTemplate.executeWithoutResult(status -> {
            if (prototype != null) {
                Video proto = videoRepository.findBySourceIdAndAuthor(prototype, author).orElseThrow(() -> new VideoNotFoundException(sourceId));
                video.setTitle(proto.getTitle());
                video.setDescription(proto.getDescription());
                video.setCategory(proto.getCategory());
                video.setAccess(proto.getAccess());
                video.setAgeRestriction(proto.isAgeRestriction());
                video.setComments(proto.isComments());
            }

            videoRepository.save(video);
        });
    }

    @Override
    public void putVideo(String sourceId, String author, VideoChangeInfo info) {
        transactionTemplate.executeWithoutResult(status -> {
            Video video = videoRepository.findBySourceIdAndAuthor(sourceId, author).orElseThrow(() -> new VideoNotFoundException(sourceId));

            if (video.getStatus() == PUBLISHED) {
                throw new VideoAlreadyPublishedException(sourceId);
            }

            video.setTitle(info.title());
            video.setDescription(info.description());
            video.setCategory(info.category());
            video.setAccess(info.access());
            video.setAgeRestriction(info.ageRestriction());
            video.setComments(info.comments());
            video.setUpdatedAt(Instant.now());

            if (video.getStatus() == FILL_PENDING) {
                video.setStatus(READY);
            }

            videoRepository.save(video);
        });
    }

    @Override
    public VideoStatus getVideoStatus(String sourceId, String author) {
        Video video = videoRepository.findBySourceIdAndAuthor(sourceId, author).orElseThrow(() -> new VideoNotFoundException(sourceId));

        return video.getStatus();
    }

    @Override
    public void publishVideo(String sourceId, String author) {
        transactionTemplate.executeWithoutResult(status -> {
            Video video = videoRepository.findBySourceIdAndAuthor(sourceId, author).orElseThrow(() -> new VideoNotFoundException(sourceId));

            if (video.getStatus() != READY) {
                throw new IncorrectVideoStatusException(video.getStatus(), READY);
            }

            video.setStatus(PUBLISHED);
            video.setPublishedAt(Instant.now());

            videoRepository.save(video);
        });
    }

    @Override
    public List<VideoThumbInfo> getPublishedVideos(PageRequest pageRequest) {
        List<Video> published = videoRepository.findAllPublished(pageRequest);
        List<VideoThumbInfo> infoList = new ArrayList<>(published.size());

        for (Video video : published) {
            VideoThumbInfo info = new VideoThumbInfo();
            info.setVideoId(video.getSourceId());
            info.setAuthor(video.getAuthor());
            info.setTitle(video.getTitle());
            info.setCategory(video.getCategory());
            info.setPublishedAt(video.getPublishedAt());

            infoList.add(info);
        }

        return infoList;
    }

    @Override
    public Video findPublishedVideo(String sourceId) {
        Video video = videoRepository.findById(sourceId).orElseThrow(() -> new VideoNotFoundException(sourceId));

        if (video.getStatus() != PUBLISHED || video.getAccess() != PUBLIC) {
            throw new VideoNotFoundException(sourceId);
        }

        return video;
    }
}
