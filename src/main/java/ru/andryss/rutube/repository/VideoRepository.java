package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.rutube.model.Video;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, String> {
    Optional<Video> findBySourceIdAndAuthor(String sourceId, String author);
}
