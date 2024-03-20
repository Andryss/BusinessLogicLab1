package ru.andryss.rutube.message;

import lombok.Data;

import java.time.Instant;

@Data
public class ModerationRequestInfo {
    String sourceId;
    String downloadLink;
    Instant createdAt;
}
