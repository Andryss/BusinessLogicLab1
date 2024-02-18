package ru.andryss.rutube.message;

import lombok.Setter;

@Setter
public class NewVideoResponse {
    String sourceId;
    String uploadLink;
}
