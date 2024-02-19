package ru.andryss.rutube.exception;

public class VideoAlreadyPublishedException extends RuntimeException {
    public VideoAlreadyPublishedException(String message) {
        super(message);
    }
}
