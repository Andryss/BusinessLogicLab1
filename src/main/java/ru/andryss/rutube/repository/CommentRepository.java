package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.rutube.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findAllBySourceIdOrderByCreatedAt(String sourceId);
}
