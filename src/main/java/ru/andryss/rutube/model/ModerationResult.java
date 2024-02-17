package ru.andryss.rutube.model;

import java.time.Instant;

public class ModerationResult {
    String sourceId;
    String assignee;
    ModerationStatus status;
    String comment;
    Instant createdAt;
}
