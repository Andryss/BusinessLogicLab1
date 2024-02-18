package ru.andryss.rutube.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.andryss.rutube.model.VideoAccess;
import ru.andryss.rutube.model.VideoCategory;

import java.time.Instant;

@Data
public class PutVideoRequest {
    @NotBlank
    @Size(max = 100)
    String title;
    @NotNull
    @Size(max = 5000)
    String description;
    @NotNull
    VideoCategory category;
    @NotNull
    VideoAccess access;
    Boolean ageRestriction;
    Boolean comments;
    Instant publication;
}
