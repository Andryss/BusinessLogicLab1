package ru.andryss.rutube.exception;

public class CommentsDisableException extends RuntimeException {
    public CommentsDisableException(String message) {
        super(message);
    }
}
