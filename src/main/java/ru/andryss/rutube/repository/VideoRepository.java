package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.rutube.model.Video;

public interface VideoRepository extends JpaRepository<Video, String> {
}
