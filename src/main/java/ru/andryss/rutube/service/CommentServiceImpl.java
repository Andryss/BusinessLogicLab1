package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.exception.CommentNotFoundException;
import ru.andryss.rutube.exception.CommentsDisableException;
import ru.andryss.rutube.exception.ParentSourceDifferentException;
import ru.andryss.rutube.exception.VideoNotFoundException;
import ru.andryss.rutube.message.CommentInfo;
import ru.andryss.rutube.message.CreateCommentRequest;
import ru.andryss.rutube.message.GetCommentsResponse;
import ru.andryss.rutube.model.Comment;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.repository.CommentRepository;
import ru.andryss.rutube.repository.VideoRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;

    @Override
    public void createComment(CreateCommentRequest request) {
        String sourceId = request.getSourceId();
        Video video = videoRepository.findById(sourceId).orElseThrow(() -> new VideoNotFoundException(sourceId));
        if (!video.isComments()) {
            throw new CommentsDisableException(sourceId);
        }


        String parentId = request.getParentId();
        if (parentId != null) {
            Comment parent = commentRepository.findById(parentId).orElseThrow(() -> new CommentNotFoundException(parentId));
            if (!parent.getSourceId().equals(sourceId)) {
                throw new ParentSourceDifferentException(parentId);
            }
        }

        Comment comment = new Comment();
        comment.setSourceId(sourceId);
        comment.setParent(parentId);
        comment.setContent(request.getContent());
        comment.setAuthor("some author");
        comment.setCreatedAt(Instant.now());

        commentRepository.save(comment);
    }

    @Override
    public GetCommentsResponse getComments(String sourceId) {
        if (!videoRepository.existsById(sourceId)) {
            throw new VideoNotFoundException(sourceId);
        }

        List<Comment> comments = commentRepository.findAllBySourceId(sourceId);

        Map<String, CommentInfo> infoById = new HashMap<>();
        Map<String, List<String>> childrenIdsById = new HashMap<>();

        comments.forEach(comment -> {
            CommentInfo info = new CommentInfo();
            info.setCommentId(comment.getId());
            info.setAuthor(comment.getAuthor());
            info.setContent(comment.getContent());
            info.setPostedAt(comment.getCreatedAt());

            infoById.put(comment.getId(), info);
            childrenIdsById.computeIfAbsent(comment.getParent(), s -> new ArrayList<>()).add(comment.getId());
        });

        List<CommentInfo> rootComments = new ArrayList<>();
        childrenIdsById.get(null).forEach(id -> rootComments.add(constructCommentInfo(id, infoById, childrenIdsById)));

        GetCommentsResponse response = new GetCommentsResponse();
        response.setComments(rootComments);
        return response;
    }

    private CommentInfo constructCommentInfo(String commentId, Map<String, CommentInfo> infoById, Map<String, List<String>> childrenIdsById) {
        ArrayList<CommentInfo> children = new ArrayList<>();
        childrenIdsById.get(commentId).forEach(id -> children.add(constructCommentInfo(id, infoById, childrenIdsById)));
        CommentInfo info = infoById.get(commentId);
        info.setChildren(children);
        return info;
    }
}
