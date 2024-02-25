package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.CreateCommentRequest;
import ru.andryss.rutube.message.GetCommentsResponse;
import ru.andryss.rutube.service.CommentService;

@Component
@RequiredArgsConstructor
public class CommentInteractorImpl implements CommentInteractor {

    private final CommentService commentService;

    @Override
    public void postApiComments(CreateCommentRequest request, User user) {
        commentService.createComment(request.getSourceId(), user.getUsername(), request.getParentId(), request.getContent());
    }

    @Override
    public GetCommentsResponse getApiComments(String sourceId, String parentId, PageRequest pageRequest) {
        GetCommentsResponse response = new GetCommentsResponse();
        response.setComments(commentService.getComments(sourceId, parentId, pageRequest));
        return response;
    }
}
