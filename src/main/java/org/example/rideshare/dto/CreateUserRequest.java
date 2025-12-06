package org.example.rideshare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank
    @Size(min = 3)
    private String username;
    @NotBlank
    @Size(min = 4)
    private String password;
    @NotBlank
    private String role; // ROLE_USER or ROLE_DRIVER
}
