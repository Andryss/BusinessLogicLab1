package ru.andryss.rutube.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
@Table(name = "reactions")
@IdClass(Reaction.ReactionKey.class)
public class Reaction {
    @Id
    String sourceId;
    @Id
    String author;
    @Enumerated(STRING)
    ReactionType type;
    Instant createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReactionKey implements Serializable {
        String sourceId;
        String author;
    }
}
