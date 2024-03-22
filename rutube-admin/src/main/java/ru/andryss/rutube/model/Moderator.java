package ru.andryss.rutube.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
@Table(name = "moderators")
public class Moderator {
    @Id
    String username;
    String password;
    String email;
    @Enumerated(STRING)
    Role role;
}
