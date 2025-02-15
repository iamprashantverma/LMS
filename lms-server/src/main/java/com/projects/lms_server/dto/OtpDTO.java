package com.projects.lms_server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OtpDTO {
    @Email(message = "please enter valid email")
    @NotNull(message = "email can't be null")
    private String email;
    private String otp;
    private String  password;
    private String message;
}
