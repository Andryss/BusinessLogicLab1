package ru.andryss.rutube.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import ru.andryss.rutube.exception.*;
import ru.andryss.rutube.message.ErrorMessage;

import static org.springframework.http.HttpStatus.*;

@ResponseBody
@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler({
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestPartException.class,
            RequestRejectedException.class
    })
    @ResponseStatus(BAD_REQUEST)
    ErrorMessage handleBadRequest(Exception e) {
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    ErrorMessage handleUnsupportedMediaType(Exception e) {
        return new ErrorMessage(e.getMessage());
    }

    @SuppressWarnings("deprecation")
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(REQUEST_ENTITY_TOO_LARGE)
    ErrorMessage handleRequestEntityTooLarge(Exception e) {
        return new ErrorMessage(e.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorMessage handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Object[] messageArguments = e.getDetailMessageArguments();
        if (messageArguments == null) {
            return new ErrorMessage(e.getMessage());
        }
        return new ErrorMessage((String) e.getDetailMessageArguments()[1]);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorMessage handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        return new ErrorMessage(String.format("request parameter %s is missing", e.getParameterName()));
    }

    @ExceptionHandler(IllegalVideoFormatException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorMessage handleIllegalVideo() {
        return new ErrorMessage("wrong video format");
    }

    @ExceptionHandler(VideoNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ErrorMessage handleVideoNotFound(VideoNotFoundException e) {
        return new ErrorMessage(String.format("video %s not found", e.getMessage()));
    }

    @ExceptionHandler(SourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ErrorMessage handleSourceNotFound(SourceNotFoundException e) {
        return new ErrorMessage(String.format("source %s not found", e.getMessage()));
    }

    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ErrorMessage handleLinkNotFount(LinkNotFoundException e) {
        return new ErrorMessage(String.format("link for id %s not found", e.getMessage()));
    }

    @ExceptionHandler(IncorrectVideoStatusException.class)
    @ResponseStatus(CONFLICT)
    ErrorMessage handleIncorrectVideoStatus(IncorrectVideoStatusException e) {
        return new ErrorMessage(String.format("video has status %s but expected %s", e.getReal(), e.getExpected()));
    }

    @ExceptionHandler(VideoAlreadyPublishedException.class)
    @ResponseStatus(CONFLICT)
    ErrorMessage handleVideoAlreadyPublished(VideoAlreadyPublishedException e) {
        return new ErrorMessage(String.format("video %s has already published", e.getMessage()));
    }

    @ExceptionHandler(CommentsDisableException.class)
    @ResponseStatus(CONFLICT)
    ErrorMessage handleCommentsDisable(CommentsDisableException e) {
        return new ErrorMessage(String.format("comments on video %s are disabled", e.getMessage()));
    }

    @ExceptionHandler(ParentSourceDifferentException.class)
    @ResponseStatus(CONFLICT)
    ErrorMessage handleParentSourceDifferent(ParentSourceDifferentException e) {
        return new ErrorMessage(String.format("parent video %s has different sourceId", e.getMessage()));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ErrorMessage handleCommentNotFound(CommentNotFoundException e) {
        return new ErrorMessage(String.format("comment %s not found", e.getMessage()));
    }
}
