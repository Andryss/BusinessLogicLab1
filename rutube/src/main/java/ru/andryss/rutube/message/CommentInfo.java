package ru.andryss.rutube.message;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentInfo {
    String commentId;
    String author;
    String content;
    int replies;
    Instant postedAt;
}
