package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.rutube.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsernameIgnoreCase(String username);
}
