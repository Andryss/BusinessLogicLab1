package ru.andryss.rutube.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;

@Getter
@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = UUID)
    String sourceId;

    String description;

    @Enumerated(STRING)
    VideoCategory category;

    @Enumerated(STRING)
    VideoAccess access;

    boolean ageRestriction;

    boolean comments;

    Instant publication;

    Instant createdAt;
}
