package com.projects.lms_server.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    private String userId;

    private String registrationId;

    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Please Enter address")
    @NotBlank(message = "Address is mandatory")
    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String address;

    @NotNull(message = "Please Enter Contact Number ")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid contact number")
    private String contactNo;

    @NotNull(message = "please Select Gender")
    @Pattern(regexp = "^(male|female|Other)$", message = "Gender must be 'male', 'female', or 'Other'")
    private String gender;
    private Boolean isActive;

    private String approvedBy;
    private LocalDate registrationDate;
    private LocalDate enrollmentDate;
    private String image;

}
