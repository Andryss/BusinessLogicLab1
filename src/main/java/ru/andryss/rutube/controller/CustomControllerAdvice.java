package ru.andryss.rutube.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.andryss.rutube.exception.*;
import ru.andryss.rutube.message.ErrorMessage;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<?> handleConstraintViolation(ConstraintViolationException e) {
        return handleError(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<?> handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        return handleError(HttpStatus.BAD_REQUEST, String.format("request parameter %s is missing", e.getParameterName()));
    }

    @ExceptionHandler(RequestValidationException.class)
    ResponseEntity<?> handleRequestValidation(RequestValidationException e) {
        return handleError(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(VideoNotFoundException.class)
    ResponseEntity<?> handleVideoNotFound(VideoNotFoundException e) {
        return handleError(HttpStatus.NOT_FOUND, String.format("video %s not found", e.getMessage()));
    }

    @ExceptionHandler(CommentsDisableException.class)
    ResponseEntity<?> handleCommentsDisable(CommentsDisableException e) {
        return handleError(HttpStatus.CONFLICT, String.format("comments on video %s are disabled", e.getMessage()));
    }

    @ExceptionHandler(ParentSourceDifferentException.class)
    ResponseEntity<?> handleParentSourceDifferent(ParentSourceDifferentException e) {
        return handleError(HttpStatus.CONFLICT, String.format("parent video %s has different sourceId", e.getMessage()));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    ResponseEntity<?> handleCommentNotFound(CommentNotFoundException e) {
        return handleError(HttpStatus.NOT_FOUND, String.format("comment %s not found", e.getMessage()));
    }

    ResponseEntity<?> handleError(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorMessage(message));
    }

}
