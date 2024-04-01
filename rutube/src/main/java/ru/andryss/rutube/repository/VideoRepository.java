package ru.andryss.rutube.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.andryss.rutube.model.Video;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, String> {
    Optional<Video> findBySourceIdAndAuthor(String sourceId, String author);

    @Query(value = "select * from videos where status = 'PUBLISHED'", nativeQuery = true)
    List<Video> findAllPublished(Pageable pageable);

    @Query(value = """
        select * from videos
        where author = :author and status in ('UPLOAD_PENDING', 'FILL_PENDING') and updated_at < :timestamp
    """, nativeQuery = true)
    List<Video> findAllPendingActions(String author, Instant timestamp);

    @Query(value = "select source_id from videos where status = 'MODERATION_PENDING' and updated_at < :timestamp", nativeQuery = true)
    List<String> findAllPendingModeration(Instant timestamp);
}
