package ru.andryss.rutube.interactor;

import ru.andryss.rutube.message.AuthRequest;
import ru.andryss.rutube.message.AuthResponse;

/**
 * Interactor for handling authentication requests
 */
public interface AuthInteractor {
    /**
     * Handles POST /api/auth/login request
     */
    AuthResponse postApiLogin(AuthRequest request);
}
