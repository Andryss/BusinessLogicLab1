package ru.andryss.rutube.message;

import lombok.Data;
import ru.andryss.rutube.model.ModerationStatus;

import java.time.Instant;

@Data
public class ModerationResultInfo {
    String sourceId;
    ModerationStatus status;
    String comment;
    Instant createdAt;
}
