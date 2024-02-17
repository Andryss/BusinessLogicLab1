package ru.andryss.rutube.model;

import java.time.Instant;

public class Reaction {
    String sourceId;
    String author;
    ReactionType type;
    Instant createdAt;
}
