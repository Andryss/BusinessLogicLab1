package ru.andryss.rutube.service;

import ru.andryss.rutube.message.ReactionInfo;
import ru.andryss.rutube.model.ReactionType;

import java.util.List;
import java.util.Optional;

/**
 * Service for working with reactions
 */
public interface ReactionService {
    /**
     * Creates reaction on given source
     *
     * @param sourceId source to create reaction
     * @param reaction type of reaction
     */
    void createReaction(String sourceId, ReactionType reaction);

    /**
     * Counts all reactions on given source
     *
     * @param sourceId source to count reactions
     * @return count of each reaction
     */
    List<ReactionInfo> getAllReactions(String sourceId);

    /**
     * Get current user reaction on given source
     * @param sourceId source to get reaction
     * @return reaction type
     */
    Optional<ReactionType> getMyReaction(String sourceId);
}
