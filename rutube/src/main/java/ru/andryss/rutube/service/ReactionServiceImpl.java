package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.message.ReactionInfo;
import ru.andryss.rutube.model.Reaction;
import ru.andryss.rutube.model.Reaction.ReactionKey;
import ru.andryss.rutube.model.ReactionType;
import ru.andryss.rutube.repository.ReactionRepository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final VideoService videoService;
    private final ReactionRepository reactionRepository;
    private final TransactionTemplate transactionTemplate;
    private final TransactionTemplate readOnlyTransactionTemplate;

    @Override
    @Retryable(retryFor = SQLException.class)
    public void createReaction(String sourceId, String author, ReactionType reactionType) {
        transactionTemplate.executeWithoutResult(status -> {
            videoService.findPublishedVideo(sourceId);

            Reaction reaction = reactionRepository.findById(new ReactionKey(sourceId, author)).orElseGet(() -> {
                Reaction r = new Reaction();
                r.setSourceId(sourceId);
                r.setAuthor(author);
                return r;
            });

            reaction.setType(reactionType);
            reaction.setCreatedAt(Instant.now());

            reactionRepository.save(reaction);
        });
    }

    @Override
    public List<ReactionInfo> getAllReactions(String sourceId) {
        return readOnlyTransactionTemplate.execute(status -> {
            videoService.findPublishedVideo(sourceId);

            return reactionRepository.findAllReactionsBySource(sourceId);
        });
    }

    @Override
    public Optional<ReactionType> getMyReaction(String sourceId, String author) {
        return readOnlyTransactionTemplate.execute(status -> {
            videoService.findPublishedVideo(sourceId);

            return reactionRepository.findById(new ReactionKey(sourceId, author)).map(Reaction::getType);
        });
    }
}
