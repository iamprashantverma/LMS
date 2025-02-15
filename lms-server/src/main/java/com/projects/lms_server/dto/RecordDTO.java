package com.projects.lms_server.dto;

import com.projects.lms_server.entites.enums.BookFormat;
import com.projects.lms_server.entites.enums.Condition;
import com.projects.lms_server.entites.enums.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecordDTO {
    private String recordId;
    private String userId;
    private String bookCopy;
    private LocalDate borrowDate;
    private Integer fine;
    private String memberName;
    private String bookTitle;
    private LocalDate returnDate;
    private Status status;
    private String issuedBy;
    private String returnedBy;
    private String isbn;
    private Condition condition;
    private BookFormat format;
    private String image;
}
