package ru.andryss.rutube.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.andryss.rutube.model.Video;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, String> {
    Optional<Video> findBySourceIdAndAuthor(String sourceId, String author);

    @Query(value = "select * from videos where status = 'PUBLISHED'", nativeQuery = true)
    List<Video> findAllPublished(Pageable pageable);
}
