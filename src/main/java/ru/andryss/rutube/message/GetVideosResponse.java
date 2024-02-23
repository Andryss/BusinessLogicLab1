package ru.andryss.rutube.message;

import lombok.Data;

import java.util.List;

@Data
public class GetVideosResponse {
    List<VideoThumbInfo> videos;
}
