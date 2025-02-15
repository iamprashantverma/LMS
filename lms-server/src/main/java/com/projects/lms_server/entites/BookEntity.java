package com.projects.lms_server.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projects.lms_server.entites.enums.BookFormat;
import com.projects.lms_server.entites.enums.Genre;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
public class BookEntity {

    @Id
    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    private String title;

    private String edition;

    private String language;

    private LocalDate publicationDate;

    private Integer pageCount;

    @Enumerated(EnumType.STRING)
    private BookFormat format;

    private Double price;

    private String image;

    private int count = 0;

    /* Many-to-One relationship with PublisherEntity */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", referencedColumnName = "publisherId", nullable = false)

    private PublisherEntity publisher;

    /* Many-to-Many relationship with AuthorEntity */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_isbn", referencedColumnName = "isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "authorId")
    )
    @ToString.Exclude
    private List<AuthorEntity> authors = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Set<Genre> genre = new HashSet<>();


}
