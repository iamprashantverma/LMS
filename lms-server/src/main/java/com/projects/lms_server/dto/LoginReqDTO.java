package com.projects.lms_server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginReqDTO {
    @Email(message = "Please Enter Valid Email")
    @NotNull(message = "please enter email")
    private String email;
    @Size(min = 5,max = 10 ,message = "Please enter Password in the valid range ")
    private String password;
}
