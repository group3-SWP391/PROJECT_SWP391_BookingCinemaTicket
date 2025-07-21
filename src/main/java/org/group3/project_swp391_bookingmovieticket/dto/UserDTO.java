package org.group3.project_swp391_bookingmovieticket.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.group3.project_swp391_bookingmovieticket.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


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

    @Email(message = "Email phải đúng định dạng")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email không hợp lệ"
    )
    private String email;
    private Role role;
    private Boolean status;
    private Integer branchId;
    
}
