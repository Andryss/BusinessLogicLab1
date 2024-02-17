package ru.andryss.rutube.exception;

public class ParentSourceDifferentException extends RuntimeException {
    public ParentSourceDifferentException(String message) {
        super(message);
    }
}
