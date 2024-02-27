package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andryss.rutube.interactor.CommentInteractor;
import ru.andryss.rutube.message.CreateCommentRequest;
import ru.andryss.rutube.message.GetCommentsResponse;

@Validated
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentInteractor commentInteractor;

    @PostMapping("/api/comments")
    public void postApiComments(
            @RequestBody @Valid CreateCommentRequest request,
            @AuthenticationPrincipal User user
    ) {
        commentInteractor.postApiComments(request, user);
    }

    @GetMapping("/api/comments")
    public GetCommentsResponse getApiComments(
            @RequestParam @NotBlank String sourceId,
            @RequestParam(required = false) String parentId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNumber,
            @RequestParam(defaultValue = "1") @Positive @Max(100) int pageSize
    ) {
        return commentInteractor.getApiComments(sourceId, parentId, PageRequest.of(pageNumber, pageSize));
    }

}
