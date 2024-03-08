package ru.andryss.rutube.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.andryss.rutube.interactor.AuthInteractor;
import ru.andryss.rutube.message.AuthRequest;
import ru.andryss.rutube.message.AuthResponse;

@Validated
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthInteractor interactor;

    @PostMapping("/api/auth/login")
    public AuthResponse postApiAuthLogin(
            @RequestBody @Valid AuthRequest request
    ) {
        return interactor.postApiLogin(request);
    }

}
