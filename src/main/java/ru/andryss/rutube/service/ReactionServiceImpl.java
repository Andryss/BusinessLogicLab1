package ru.andryss.rutube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.exception.VideoNotFoundException;
import ru.andryss.rutube.message.ReactionInfo;
import ru.andryss.rutube.model.Reaction;
import ru.andryss.rutube.model.Reaction.ReactionKey;
import ru.andryss.rutube.model.ReactionType;
import ru.andryss.rutube.repository.ReactionRepository;
import ru.andryss.rutube.repository.VideoRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final VideoRepository videoRepository;
    private final ReactionRepository reactionRepository;

    @Override
    public void createReaction(String sourceId, ReactionType reactionType) {
        if (!videoRepository.existsById(sourceId)) {
            throw new VideoNotFoundException(sourceId);
        }

        // TODO: determine user
        String username = "username";

        Reaction reaction = reactionRepository.findById(new ReactionKey(sourceId, username)).orElseGet(() -> {
            Reaction r = new Reaction();
            r.setSourceId(sourceId);
            r.setAuthor(username);
            return r;
        });
        
        reaction.setType(reactionType);
        reaction.setCreatedAt(Instant.now());

        reactionRepository.save(reaction);
    }

    @Override
    public List<ReactionInfo> getAllReactions(String sourceId) {
        if (!videoRepository.existsById(sourceId)) {
            throw new VideoNotFoundException(sourceId);
        }
        
        Map<ReactionType, Long> reactionsCount = reactionRepository.findAllReactionsBySource(sourceId).stream()
                .collect(groupingBy(identity(), counting()));
        
        List<ReactionInfo> infoList = new ArrayList<>();
        reactionsCount.forEach((r, c) -> infoList.add(new ReactionInfo(r, c)));
        
        return infoList;
    }

    @Override
    public Optional<ReactionType> getMyReaction(String sourceId) {
        if (!videoRepository.existsById(sourceId)) {
            throw new VideoNotFoundException(sourceId);
        }
        
        // TODO: determine user
        String username = "username";
        
        return reactionRepository.findById(new ReactionKey(sourceId, username)).map(Reaction::getType);
    }
}