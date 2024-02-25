package ru.andryss.rutube.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import static jakarta.persistence.GenerationType.UUID;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = UUID)
    String id;
    String sourceId;
    String parent;
    String content;
    String author;
    int replies;
    Instant createdAt;
}
