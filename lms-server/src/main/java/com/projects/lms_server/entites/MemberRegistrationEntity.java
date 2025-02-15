package com.projects.lms_server.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

import static com.projects.lms_server.utils.GenerateID.generateId;

@Entity
@Table(name = "member_registration")
@Data
public class MemberRegistrationEntity {
    private static final String  PREFIX = "REG-";

    @Id
    private  String registrationId;
    private String name;
    private String email;
    private String address;
    private String contactNo;
    private String gender;
    private LocalDate registrationDate = LocalDate.now();

    @PrePersist
    private void assignRegistrationId() {
        this.registrationId = PREFIX+generateId();
    }


}
