package com.projects.lms_server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorDTO {

    private String authorId;
    @NotBlank(message = "please enter valid name")
    @NotNull(message = "please enter name")
    private String name;
    @PastOrPresent(message = "please enter valid DOB")
    @NotNull(message = "enter DOB")
    private LocalDate dateOfBirth;
    @NotNull(message = "enter nationality of author")
    @NotBlank(message = "please enter valid nationality")
    private String nationality;
    @Email(message = "please enter valid email")
    @NotNull(message = "please enter email")
    private String email;

}
