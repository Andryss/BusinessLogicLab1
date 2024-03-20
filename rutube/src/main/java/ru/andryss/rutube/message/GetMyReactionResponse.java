package ru.andryss.rutube.message;

import lombok.Data;
import ru.andryss.rutube.model.ReactionType;

@Data
public class GetMyReactionResponse {
    ReactionType reaction;
}
