package ru.andryss.rutube.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.andryss.rutube.model.ReactionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionInfo {
    ReactionType reaction;
    long count;
}
