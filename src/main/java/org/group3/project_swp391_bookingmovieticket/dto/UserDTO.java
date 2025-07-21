package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.Data;
import org.group3.project_swp391_bookingmovieticket.entity.Role;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import javax.validation.constraints.Email;


@Data
public class UserDTO {

    private Integer id;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters!")
    private String password;

    @NotBlank(message = "Fullname cannot be blank")
    private String fullname;

    @NotBlank(message = "Phone cannot be blank")
    @Size(min = 9, max = 11, message = "Phone not valid!")
    private String phone;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;

    private Role role;
    
}
