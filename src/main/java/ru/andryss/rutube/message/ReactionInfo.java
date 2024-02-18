package ru.andryss.rutube.message;

import lombok.AllArgsConstructor;
import lombok.Setter;
import ru.andryss.rutube.model.ReactionType;

@Setter
@AllArgsConstructor
public class ReactionInfo {
    ReactionType reaction;
    long count;
}
