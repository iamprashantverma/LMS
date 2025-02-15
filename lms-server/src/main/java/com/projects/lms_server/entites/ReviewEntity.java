package com.projects.lms_server.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

import static com.projects.lms_server.utils.GenerateID.generateId;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
public class ReviewEntity {
    private final static String PREFIX = "PRE-";
    @Id
    @Column(name = "id", nullable = false)
    private String reviewId;

    @PrePersist
    private void assignId(){
        this.reviewId = PREFIX + generateId();
    }

    private Double rating;
    private String comment;

    private LocalDate reviewDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_isbn", referencedColumnName = "isbn", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private BookEntity book;

}
