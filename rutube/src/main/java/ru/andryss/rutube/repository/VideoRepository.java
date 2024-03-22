package ru.andryss.rutube.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.andryss.rutube.model.User;
import ru.andryss.rutube.model.Video;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, String> {
    Optional<Video> findBySourceIdAndAuthor(String sourceId, String author);

    @Query(value = "select * from videos where status = 'PUBLISHED'", nativeQuery = true)
    List<Video> findAllPublished(Pageable pageable);

    @Query(value = """
        select u from videos as v join users as u on v.author = u.username
        where v.status in ('UPLOAD_PENDING', 'FILL_PENDING') and v.updated_at < :timestamp
    """, nativeQuery = true)
    List<User> findUsersWithPendingActions(Instant timestamp);

    @Query(value = "select * from videos where author = :author and status in ('UPLOAD_PENDING', 'FILL_PENDING') and updated_at < :timestamp", nativeQuery = true)
    List<Video> findAllPendingActions(String author, Instant timestamp);
}
