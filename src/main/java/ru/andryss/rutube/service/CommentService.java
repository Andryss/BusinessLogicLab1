package ru.andryss.rutube.service;

import ru.andryss.rutube.message.CommentInfo;

import java.util.List;

/**
 * Service for working with comments
 */
public interface CommentService {
    /**
     * Creates a comment
     *
     * @param sourceId source to post comment
     * @param parentId parent comment (in case of reply)
     * @param content comment content
     */
    void createComment(String sourceId, String parentId, String content);

    /**
     * Gets comments on given source
     *
     * @param sourceId source to search
     * @return list of root comments
     */
    List<CommentInfo> getComments(String sourceId);
}
