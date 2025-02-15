package com.projects.lms_server.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projects.lms_server.entites.enums.BookFormat;
import com.projects.lms_server.entites.enums.Condition;
import com.projects.lms_server.entites.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

import static com.projects.lms_server.utils.GenerateID.generateId;

@Entity
@Table(name = "book_copy")
@Data
public class BookCopyEntity {

    private static final String PREFIX = "BOOK-";

    @Id
    @Column(nullable = false, unique = true)
    private String bookId;

    @PrePersist
    private void assignBookId() {
        this.bookId = PREFIX + generateId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn", referencedColumnName = "isbn", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private BookEntity book;

    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "book_condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private Condition condition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", referencedColumnName = "userId")
    @JsonIgnore
    @ToString.Exclude
    private UserEntity borrower;
    private BookFormat format;
    private LocalDate dueDate;


}
