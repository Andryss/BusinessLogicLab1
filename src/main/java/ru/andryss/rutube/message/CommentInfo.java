package ru.andryss.rutube.message;

import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
public class CommentInfo {
    String commentId;
    String author;
    String content;
    Instant postedAt;
    List<CommentInfo> children;
}
