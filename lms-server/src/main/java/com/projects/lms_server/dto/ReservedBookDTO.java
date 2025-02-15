package com.projects.lms_server.dto;

import com.projects.lms_server.entites.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class ReservedBookDTO {

    @NotNull(message = "please bookId")
    @NotBlank(message = "please enter book ID")
    private String bookCopy;
    private String  reservationId;
    private Status status;
    private String user;
    private String image;
    private String bookTitle;
    private String memberName;
    private  String reservationDate;

}
