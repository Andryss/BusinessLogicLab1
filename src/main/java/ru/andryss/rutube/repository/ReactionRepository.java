package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.andryss.rutube.message.ReactionInfo;
import ru.andryss.rutube.model.Reaction;
import ru.andryss.rutube.model.Reaction.ReactionKey;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, ReactionKey> {
    @Query("select new ru.andryss.rutube.message.ReactionInfo(r.type, count(r)) from Reaction r where r.sourceId = :sourceId group by r.type")
    List<ReactionInfo> findAllReactionsBySource(String sourceId);
}
