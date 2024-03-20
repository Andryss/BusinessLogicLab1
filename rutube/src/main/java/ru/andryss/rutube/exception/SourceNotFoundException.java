package ru.andryss.rutube.exception;

public class SourceNotFoundException extends RuntimeException {
    public SourceNotFoundException(String message) {
        super(message);
    }
}
