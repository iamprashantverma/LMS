package com.projects.lms_server.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static com.projects.lms_server.utils.GenerateID.generateId;


@Entity
@Table(name = "otp")
@Data
public class OTPEntity {

    @Id
    private String id;

    @PrePersist
    private void assignId(){
        this.id =   generateId();
    }

    @CreationTimestamp
    private LocalDateTime timeStamp;
    private String email;
    private String otp;


}
