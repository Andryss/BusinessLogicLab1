package ru.andryss.rutube.message;

import lombok.Data;
import ru.andryss.rutube.model.VideoStatus;

@Data
public class GetVideoStatusResponse {
    VideoStatus status;
    String comment;
}
