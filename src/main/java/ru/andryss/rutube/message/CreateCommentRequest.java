package ru.andryss.rutube.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class CreateCommentRequest {
    @NotBlank
    @UUID
    String sourceId;
    String parentId;
    @NotBlank
    String content;
}
