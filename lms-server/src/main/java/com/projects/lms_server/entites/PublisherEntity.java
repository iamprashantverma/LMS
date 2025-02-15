package com.projects.lms_server.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.projects.lms_server.utils.GenerateID.generateId;

@Entity
@Table(name = "publisher")
@Data
public class PublisherEntity {

    private static String PREFIX = "PUB-";
    @Id
    private  String publisherId;
    @PrePersist
    private void assignPublisherId(){
        this.publisherId = PREFIX+generateId();
    }
    private String email;
    private String name;
    private String address;

    /* Creating relation between book entity and publisher */
    @OneToMany(mappedBy = "publisher",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<BookEntity> books = new ArrayList<>();

}
