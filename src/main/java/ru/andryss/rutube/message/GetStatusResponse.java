package ru.andryss.rutube.message;

import lombok.Setter;
import ru.andryss.rutube.model.VideoStatus;

@Setter
public class GetStatusResponse {
    VideoStatus status;
}
