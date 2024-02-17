package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.andryss.rutube.exception.RequestValidationException;
import ru.andryss.rutube.message.CreateCommentRequest;
import ru.andryss.rutube.message.GetCommentsResponse;
import ru.andryss.rutube.service.CommentService;

@Validated
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/comments")
    public ResponseEntity<?> postApiComments(
            @RequestBody @Valid CreateCommentRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        commentService.createComment(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/comments")
    public ResponseEntity<?> getApiComments(
            @RequestParam @NotBlank @UUID String sourceId
    ) {
        return ResponseEntity.ok(commentService.getComments(sourceId));
    }

}
