package ru.andryss.rutube.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.rutube.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findAllBySourceIdAndParentNull(String sourceId, Pageable pageable);
    boolean existsBySourceIdAndParent(String sourceId, String parentId);
    List<Comment> findAllBySourceIdAndParent(String sourceId, String parentId, Pageable pageable);
}
