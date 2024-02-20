package ru.andryss.rutube.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sources")
public class Source {
    @Id
    String sourceId;
    byte[] content;
}
