package org.group3.project_swp391_bookingmovieticket.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserLoginDTO {

    @Email(message = "Eamil not valid!")
    @NotBlank(message = "Email cannot be blank")
    private String emailLogin;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters!")
    private String passwordLogin;
}
