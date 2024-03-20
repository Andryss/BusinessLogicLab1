package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.rutube.model.ModerationHistory;

public interface ModerationHistoryRepository extends JpaRepository<ModerationHistory, String> {
}
