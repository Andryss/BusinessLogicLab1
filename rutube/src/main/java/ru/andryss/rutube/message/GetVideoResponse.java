package ru.andryss.rutube.message;

import lombok.Data;
import ru.andryss.rutube.model.VideoCategory;

import java.time.Instant;

@Data
public class GetVideoResponse {
    String downloadLink;
    String author;
    String title;
    String description;
    VideoCategory category;
    boolean ageRestriction;
    boolean comments;
    Instant publishedAt;
}
