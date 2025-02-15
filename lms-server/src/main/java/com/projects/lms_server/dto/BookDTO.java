package com.projects.lms_server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projects.lms_server.entites.enums.BookFormat;
import com.projects.lms_server.entites.enums.Genre;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class BookDTO {

    private String isbn;
    @NotNull(message = "enter the book title")
    @NotBlank(message = "enter e valid title")
    private String title;

    @NotNull(message = "enter the book edition")
    @NotBlank(message = "enter e valid edition")
    private String edition;
    @NotNull(message = "enter the book language")
    @NotBlank(message = "enter e valid lanuguage")
    private String language;
    @PastOrPresent(message = "please enter valid publication date")
    @NotNull(message = "enter the publication Data")
    private LocalDate publicationDate;

    @Min(value = 1,message = "Enter the Valid page count")
    @NotNull(message = "please enter the total count of pages in the book")
    private Integer pageCount;

    @NotNull(message = "please provide the format of the book ")
    private BookFormat format;

    @NotNull(message = "enter the price")
    @Min(value = 0,message = "please enter valid price")
    private Double price;

    private String image;


    @NotNull(message = "please enter the count of this book")
    @Min(value = 1,message = "Please enter the count in the range")
    private Integer count ;

    @NotNull(message = "Please enter the publisher ")
    @NotBlank(message = "please enter the valid publisher")
    private String publisherId;

    private PublisherDTO publisher;

    @Size(min = 1,message = "please enter the id of authors")
    @NotNull(message = "please enter the authors")
    private Set<String> authors = new HashSet<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Set<Genre> genre = new HashSet<>();

}
