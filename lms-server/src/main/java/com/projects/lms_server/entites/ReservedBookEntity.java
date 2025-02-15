package com.projects.lms_server.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projects.lms_server.entites.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.projects.lms_server.utils.GenerateID.generateId;

@Entity
@Table(name = "reserved_books")
@Data
@NoArgsConstructor
public class ReservedBookEntity {

    private final static String PREFIX = "RES_BOOK-";

    @Id
    private String reservationId;

    @PrePersist
    private void assignReservationId() {
        this.reservationId = PREFIX + generateId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_copy_id", referencedColumnName = "bookId", nullable = false)
    @JsonIgnore
    private BookCopyEntity bookCopy;
    @Enumerated(EnumType.STRING)
    private Status status;
    private  String image;

    private LocalDate reservationDate ;


}
