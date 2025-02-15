package com.projects.lms_server.dto;

import com.projects.lms_server.entites.enums.BookFormat;
import com.projects.lms_server.entites.enums.Condition;
import com.projects.lms_server.entites.enums.Status;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class BookCopyDTO {

    private String bookId;

    private String bookIsbn;

    private String bookTitle;

    private Status status;

    private Condition condition;

    private String borrowerId;

    private String borrowerName;

    private LocalDate dueDate;

    private Boolean isAvailable;

    private BookFormat format;
    private String image;

}
