package ru.andryss.rutube.message;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class CommentInfo {
    String commentId;
    String author;
    String content;
    Instant postedAt;
    List<CommentInfo> children;
}
