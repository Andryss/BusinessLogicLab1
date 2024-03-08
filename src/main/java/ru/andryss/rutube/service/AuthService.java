package ru.andryss.rutube.service;

/**
 * Service for working with authentication
 */
public interface AuthService {
    /**
     * Logs in for given user
     *
     * @param username username to log in
     * @param password password to log in
     * @return generated JWT token
     */
    String loginUser(String username, String password);
}
