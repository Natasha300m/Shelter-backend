package com.shelter.mykyda.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDto {

    @Email(message = "Please provide a valid email address.")
    @NotBlank(message = "User should contain an E-mail!")
    private String email;

    @NotBlank(message = "User should contain a password!")
    @Size(min = 8,max = 50, message = "Password should have at least 8 characters!")
    private String password;

    @NotBlank(message = "User should contain a role ")
    @Pattern(regexp = "VOLUNTEER|MANAGER", message = "Role must be one of: VOLUNTEER/MANAGER")
    private String role;
}
