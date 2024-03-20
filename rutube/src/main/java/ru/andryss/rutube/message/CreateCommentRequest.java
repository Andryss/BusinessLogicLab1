package ru.andryss.rutube.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class CreateCommentRequest {
    @NotBlank
    @UUID
    String sourceId;
    @UUID
    String parentId;
    @NotBlank
    @Size(max = 200)
    String content;
}
