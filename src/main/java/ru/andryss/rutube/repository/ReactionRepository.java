package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.andryss.rutube.model.Reaction;
import ru.andryss.rutube.model.Reaction.ReactionKey;
import ru.andryss.rutube.model.ReactionType;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, ReactionKey> {
    @Query(value = "select type from reactions where source_id = :sourceId", nativeQuery = true)
    List<ReactionType> findAllReactionsBySource(String sourceId);
}
