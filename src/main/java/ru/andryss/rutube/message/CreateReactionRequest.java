package ru.andryss.rutube.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;
import ru.andryss.rutube.model.ReactionType;

@Data
public class CreateReactionRequest {
    @NotBlank
    @UUID
    String sourceId;
    @NotNull
    ReactionType reaction;
}
