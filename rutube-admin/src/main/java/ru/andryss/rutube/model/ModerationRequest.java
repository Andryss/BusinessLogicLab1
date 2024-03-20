package ru.andryss.rutube.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "moderation_requests")
public class ModerationRequest {
    @Id
    String sourceId;

    String downloadLink;

    String assignee;

    Instant createdAt;
    
    Instant assignedAt;
}
