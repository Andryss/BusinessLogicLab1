package ru.andryss.rutube.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
@Table(name = "videos")
public class Video {
    @Id
    String sourceId;

    String author;

    String title;

    String description;

    @Enumerated(STRING)
    VideoCategory category;

    @Enumerated(STRING)
    VideoAccess access;

    boolean ageRestriction = false;

    boolean comments = true;

    @Enumerated(STRING)
    VideoStatus status;

    Instant createdAt;

    Instant updatedAt;

    Instant publishedAt;
}
