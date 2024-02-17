package ru.andryss.rutube.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import static jakarta.persistence.GenerationType.UUID;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = UUID)
    String id;
    String sourceId;
    String parent;
    String content;
    String author;
    Instant createdAt;
}
