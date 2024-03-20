package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.rutube.model.Moderator;

import java.util.Optional;

public interface ModeratorRepository extends JpaRepository<Moderator, String> {
    Optional<Moderator> findByUsernameIgnoreCase(String username);
}
