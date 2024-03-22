package ru.andryss.rutube.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    String username;
    String password;
    String email;
    @Enumerated(STRING)
    Role role;
}
