package ru.andryss.rutube.exception;

import lombok.Getter;
import ru.andryss.rutube.model.VideoStatus;

@Getter
public class IncorrectVideoStatusException extends RuntimeException {

    private final VideoStatus real;
    private final VideoStatus expected;

    public IncorrectVideoStatusException(VideoStatus real, VideoStatus expected) {
        this.real = real;
        this.expected = expected;
    }
}
