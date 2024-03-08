package ru.andryss.rutube.message;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class AuthRequest {
    @NotBlank
    String username;
    @NotBlank
    String password;
}
