package ru.andryss.rutube.exception;

public class LinkNotFoundException extends RuntimeException {
    public LinkNotFoundException(String message) {
        super(message);
    }
}
