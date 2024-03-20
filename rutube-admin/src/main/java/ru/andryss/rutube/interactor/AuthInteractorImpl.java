package ru.andryss.rutube.interactor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.message.AuthRequest;
import ru.andryss.rutube.message.AuthResponse;
import ru.andryss.rutube.service.AuthService;

@Component
@RequiredArgsConstructor
public class AuthInteractorImpl implements AuthInteractor {

    private final AuthService authService;

    @Override
    public AuthResponse postApiLogin(AuthRequest request) {
        AuthResponse response = new AuthResponse();
        response.setToken(authService.loginUser(request.getUsername(), request.getPassword()));
        return response;
    }
}
