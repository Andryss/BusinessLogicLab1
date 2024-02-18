package ru.andryss.rutube.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Entity
@IdClass(Reaction.ReactionKey.class)
public class Reaction {
    @Id
    String sourceId;
    @Id
    String author;
    ReactionType type;
    Instant createdAt;

    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReactionKey implements Serializable {
        String sourceId;
        String author;
    }
}
