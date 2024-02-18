package ru.andryss.rutube.message;

import lombok.Setter;
import ru.andryss.rutube.model.ReactionType;

@Setter
public class GetMyReactionResponse {
    ReactionType reaction;
}
