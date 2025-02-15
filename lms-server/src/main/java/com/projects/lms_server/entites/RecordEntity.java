package com.projects.lms_server.entites;

import com.projects.lms_server.entites.enums.Condition;
import com.projects.lms_server.entites.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.projects.lms_server.utils.GenerateID.generateId;

@Entity
@Table(name = "record")
@Data
@RequiredArgsConstructor
public class RecordEntity {
    private final static String PREFIX = "REC-";
    @Id
    private String recordId;

    @PrePersist
    private void assigningRecordId() {
        this.recordId = PREFIX + generateId();
    }

    private LocalDate borrowDate = LocalDate.now();

    private LocalDate returnDate;

    private Integer fine ;

    @Enumerated(EnumType.STRING)
    private Status status ;

    @Column(name = "book_condition")
    @Enumerated(EnumType.STRING)
    private Condition condition;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_copy", referencedColumnName = "bookId")
    private BookCopyEntity bookCopy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by", referencedColumnName = "userId")
    private UserEntity issuedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "returned_by", referencedColumnName = "userId")
    private UserEntity returnedBy;

    private String isbn;



}
