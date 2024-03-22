package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.andryss.rutube.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsernameIgnoreCase(String username);

    @Query(value = """
        select u.username, u.email, u.password, u.role
        from videos as v join users as u on v.author = u.username
        where v.status in ('UPLOAD_PENDING', 'FILL_PENDING') and v.updated_at < :timestamp
    """, nativeQuery = true)
    List<User> findWithVideosPendingActions(Instant timestamp);
}
