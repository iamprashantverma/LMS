package com.projects.lms_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ReviewDTO {

    private String  reviewId;

    private String bookId;

    @Size(min = 0,max = 5,message = "please give the rating between 1 - 5")
    @NotNull(message = "please give rating")
    private Double rating;

    @NotBlank(message = "please enter valid comment")
    @NotNull(message = "please enter comment")
    private String comment;

    private LocalDate reviewDate = LocalDate.now();

    private UserDTO user;


}
