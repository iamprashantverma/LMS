package com.projects.lms_server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PublisherDTO {
    private String publisherId;
    @Email(message = "please enter valid email")
    @NotNull(message = "please enter email")
    private String email;
    @NotBlank(message = "please enter valid name")
    @NotNull(message = "please enter name")
    private String name;
    @NotNull(message = "enter publisher address")
    @NotBlank(message = "enter valid address")
    private String address;
}
