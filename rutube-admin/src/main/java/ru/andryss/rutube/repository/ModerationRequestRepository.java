package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.andryss.rutube.model.ModerationRequest;

import java.time.Instant;
import java.util.Optional;

public interface ModerationRequestRepository extends JpaRepository<ModerationRequest, String> {
    Optional<ModerationRequest> findByAssignee(String username);

    @Modifying
    @Query(value = """
        update moderation_requests
        set assignee = :username, assigned_at = :assignedAt
        where source_id = (select source_id from moderation_requests where assignee is null limit 1)
        """, nativeQuery = true)
    void assignModeration(String username, Instant assignedAt);
}
