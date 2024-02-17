package ru.andryss.rutube.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class RequestValidationException extends RuntimeException {

    private final BindingResult bindingResult;

    public RequestValidationException(BindingResult bindingResult) {
        super();
        this.bindingResult = bindingResult;
    }
}
