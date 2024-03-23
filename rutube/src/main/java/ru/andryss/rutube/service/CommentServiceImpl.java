package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.exception.CommentNotFoundException;
import ru.andryss.rutube.exception.CommentsDisableException;
import ru.andryss.rutube.exception.ParentSourceDifferentException;
import ru.andryss.rutube.message.CommentInfo;
import ru.andryss.rutube.model.Comment;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.repository.CommentRepository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.by;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final VideoService videoService;
    private final TransactionTemplate transactionTemplate;
    private final TransactionTemplate readOnlyTransactionTemplate;

    @Override
    @Retryable(retryFor = SQLException.class)
    public void createComment(String sourceId, String author, String parentId, String content) {
        transactionTemplate.executeWithoutResult(status -> {
            Video video = videoService.findPublishedVideo(sourceId);
            if (!video.isComments()) {
                throw new CommentsDisableException(sourceId);
            }

            if (parentId != null) {
                Comment parent = commentRepository.findById(parentId).orElseThrow(() -> new CommentNotFoundException(parentId));
                if (!parent.getSourceId().equals(sourceId)) {
                    throw new ParentSourceDifferentException(parentId);
                }
                parent.setReplies(parent.getReplies() + 1);
                commentRepository.save(parent);
            }

            Comment comment = new Comment();
            comment.setSourceId(sourceId);
            comment.setParent(parentId);
            comment.setContent(content);
            comment.setAuthor(author);
            comment.setCreatedAt(Instant.now());

            commentRepository.save(comment);
        });
    }

    @Override
    public List<CommentInfo> getComments(String sourceId, String parentId, PageRequest pageRequest) {
        return readOnlyTransactionTemplate.execute(status -> {
            Video video = videoService.findPublishedVideo(sourceId);
            if (!video.isComments()) {
                throw new CommentsDisableException(sourceId);
            }

            PageRequest page = pageRequest.withSort(by("createdAt"));
            List<Comment> comments;
            if (parentId == null) {
                comments = commentRepository.findAllBySourceIdAndParentNull(sourceId, page);
            } else {
                if (!commentRepository.existsBySourceIdAndParent(sourceId, parentId)) {
                    throw new CommentNotFoundException(parentId);
                }
                comments = commentRepository.findAllBySourceIdAndParent(sourceId, parentId, page);
            }

            List<CommentInfo> commentInfos = new ArrayList<>(comments.size());
            for (Comment comment : comments) {
                CommentInfo info = new CommentInfo();
                info.setCommentId(comment.getId());
                info.setAuthor(comment.getAuthor());
                info.setContent(comment.getContent());
                info.setReplies(comment.getReplies());
                info.setPostedAt(comment.getCreatedAt());
                commentInfos.add(info);
            }

            return commentInfos;
        });
    }
}
