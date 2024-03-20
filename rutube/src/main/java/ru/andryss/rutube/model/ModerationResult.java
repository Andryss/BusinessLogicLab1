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
@Table(name = "moderation_results")
public class ModerationResult {
    @Id
    String sourceId;

    @Enumerated(STRING)
    ModerationStatus status;

    String comment;

    Instant createdAt;
}
