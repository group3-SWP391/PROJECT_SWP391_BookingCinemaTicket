package org.group3.project_swp391_bookingmovieticket.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserRegisterDTO {

    @Email(message = "Eamil not valid!")
    @NotBlank(message = "Email cannot be blank!")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters!")
    @NotBlank(message = "Password cannot be blank!")
    private String password;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 6, max = 100, message = "Username must be >= 6 characters and < 100 characters!")
    private String userName;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, max = 100, message = "Full name must be >= 2 characters and < 100 characters!")
    private String fullName;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits!")
    private String phone;

}
