package ru.andryss.rutube.message;

import lombok.Data;

import java.util.List;

@Data
public class GetCommentsResponse {
    List<CommentInfo> comments;
}
