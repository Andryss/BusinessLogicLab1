package ru.andryss.rutube.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import ru.andryss.rutube.exception.SourceNotFoundException;
import ru.andryss.rutube.message.ErrorMessage;

import static org.springframework.http.HttpStatus.*;

@Slf4j
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
        log.info("caught {} returned 400 ({})", e.getClass().getName(), e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    ErrorMessage handleUnsupportedMediaType(Exception e) {
        log.info("caught HttpMediaTypeNotSupportedException returned 415 ({})", e.getMessage());
        return new ErrorMessage(e.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorMessage handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Object[] messageArguments = e.getDetailMessageArguments();
        if (messageArguments == null) {
            return new ErrorMessage(e.getMessage());
        }
        String message = (String) e.getDetailMessageArguments()[1];
        log.info("caught MethodArgumentNotValidException returned 400 ({})", message);
        return new ErrorMessage(message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorMessage handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        log.info("caught MissingServletRequestParameterException returned 400 ({})", e.getParameterName());
        return new ErrorMessage(String.format("request parameter %s is missing", e.getParameterName()));
    }

    @ExceptionHandler(SourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ErrorMessage handleSourceNotFound(SourceNotFoundException e) {
        log.info("caught SourceNotFoundException returned 404 ({})", e.getMessage());
        return new ErrorMessage(String.format("source %s not found", e.getMessage()));
    }
}
