package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.rutube.model.ModerationResult;

public interface ModerationResultRepository extends JpaRepository<ModerationResult, String> {
}
