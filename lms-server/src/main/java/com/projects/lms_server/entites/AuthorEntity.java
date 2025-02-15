package com.projects.lms_server.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import static com.projects.lms_server.utils.GenerateID.generateId;

@Entity
@Table(name = "author")
@Data
public class AuthorEntity {

    private final static String PREFIX ="AUTH-";
    @Id
    private String authorId;
    @PrePersist
    private void assignPublisherId(){
        this.authorId = PREFIX+generateId();
    }

    private String name;
    private LocalDate dateOfBirth;
    private String nationality;
    private String email;

}
