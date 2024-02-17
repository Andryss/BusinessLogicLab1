package ru.andryss.rutube.service;

import ru.andryss.rutube.message.CreateCommentRequest;
import ru.andryss.rutube.message.GetCommentsResponse;

public interface CommentService {
    void createComment(CreateCommentRequest request);
    GetCommentsResponse getComments(String sourceId);
}
