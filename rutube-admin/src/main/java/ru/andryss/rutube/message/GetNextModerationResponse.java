package ru.andryss.rutube.message;

import lombok.Data;

@Data
public class GetNextModerationResponse {
    String sourceId;
    String downloadLink;
}
