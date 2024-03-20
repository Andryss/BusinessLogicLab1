package ru.andryss.rutube.message;

import lombok.Data;
import ru.andryss.rutube.model.VideoCategory;

import java.time.Instant;

@Data
public class VideoThumbInfo {
    String videoId;
    String author;
    String title;
    VideoCategory category;
    Instant publishedAt;
}
