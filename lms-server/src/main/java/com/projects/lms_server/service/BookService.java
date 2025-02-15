package com.projects.lms_server.service;

import com.projects.lms_server.dto.BookDTO;
import com.projects.lms_server.entites.AuthorEntity;
import com.projects.lms_server.entites.BookCopyEntity;
import com.projects.lms_server.entites.BookEntity;
import com.projects.lms_server.entites.PublisherEntity;
import com.projects.lms_server.entites.enums.BookFormat;
import com.projects.lms_server.entites.enums.Condition;
import com.projects.lms_server.entites.enums.Genre;
import com.projects.lms_server.entites.enums.Status;
import com.projects.lms_server.exceptions.ResourceNotFoundException;
import com.projects.lms_server.repository.AuthorRepository;
import com.projects.lms_server.repository.BookCopyRepository;
import com.projects.lms_server.repository.BookRepository;
import com.projects.lms_server.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.projects.lms_server.entites.enums.Condition.IMMORTAL;
import static com.projects.lms_server.entites.enums.Status.PERPETUALLY_AVAILABLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    private final BookCopyRepository    bookCopyRepository;
    private final AuthorRepository  authorRepository;
    private final PublisherRepository publisherRepository;
    private final CloudinaryService cloudinaryService;

    private BookEntity convertToEntity(BookDTO bookDTO){
        return modelMapper.map(bookDTO,BookEntity.class);
    }

    private BookDTO convertToDTO(BookEntity book) {
        return modelMapper.map(book,BookDTO.class);
    }

    /* adding book into the library */
    @Transactional
    public BookDTO addNewBook(BookDTO bookDTO, Integer count,MultipartFile file) throws IOException {

        String isbn = bookDTO.getIsbn();
        // Check if the book already exists
        Optional<BookEntity> optionalBook = bookRepository.findById(isbn);
        // Fetch authors
        List<AuthorEntity> authors = bookDTO.getAuthors().stream()
                .map(authorId -> authorRepository.findById(authorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid author id: " + authorId)))
                .collect(Collectors.toList());

        // Fetch publisher
        PublisherEntity publisher = publisherRepository.findById(bookDTO.getPublisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid publisher id: " + bookDTO.getPublisher()));

        // Prepare the book entity
        BookEntity savedBook;
        if (optionalBook.isEmpty()) {
            /* creating the new Book */
            savedBook = convertToEntity(bookDTO);
        } else {
            // Existing book
            savedBook = optionalBook.get();
            if (savedBook.getFormat() != bookDTO.getFormat()) {
                throw new IllegalStateException("Book format could be either only EBOOK or HARDCOVER");
            } else if (bookDTO.getFormat() == BookFormat.EBOOK)
                    return convertToDTO(savedBook);
        }

        // Update count
        if (bookDTO.getFormat() != BookFormat.EBOOK) {
            savedBook.setCount(savedBook.getCount() + count);
        } else {
            savedBook.setCount(1);
        }

        /* updating or adding the author,publisher,genre and Format */
        savedBook.setAuthors(authors);
        savedBook.setPublisher(publisher);
        savedBook.setGenre(bookDTO.getGenre());
        savedBook.setFormat(bookDTO.getFormat());
        /* uploading the image on to Cloudinary and getting the image url*/
        String imgUrl = cloudinaryService.uploadImage(file);
        savedBook.setImage(imgUrl);
        savedBook = bookRepository.save(savedBook);

        // Create copies
        if (bookDTO.getFormat() != BookFormat.EBOOK) {
            while (count > 0) {
                count--;
                BookCopyEntity bookCopy = new BookCopyEntity();
                bookCopy.setBook(savedBook);
                bookCopy.setCondition(Condition.GOOD);
                bookCopy.setStatus(Status.AVAILABLE);
                bookCopy.setFormat(bookDTO.getFormat());
                bookCopy.setImage(imgUrl);
                bookCopyRepository.save(bookCopy);
            }
        } else {
            /* if format of book is EBOOK */
            BookCopyEntity bookCopy = new BookCopyEntity();
            bookCopy.setBook(savedBook);
            bookCopy.setCondition(Condition.IMMORTAL);
            bookCopy.setStatus(Status.PERPETUALLY_AVAILABLE);
            bookCopy.setFormat(bookDTO.getFormat());
            bookCopy.setImage(imgUrl);
            bookCopyRepository.save(bookCopy);
        }

        log.info("Book with ISBN {} has been saved with genre: {}", savedBook.getIsbn(), savedBook.getGenre());
        return modelMapper.map(savedBook, BookDTO.class);

    }

    /* get all books by Genre*/
    public List<BookDTO> getBooksByGenre(Genre genre) {

        List<BookEntity> books = bookRepository.findBooksByGenre(genre);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No books found for genre: " + genre);
        }
        // Convert to DTOs
        return books.stream()
                .map(this::convertToDTO)
                .toList();

    }

    /* find all books */
    public List<BookDTO> getAllBooks () {
        List<BookEntity> bks = bookRepository.findAll();
        return bks.stream()
                .map(this::convertToDTO)
                .toList();
    }

    /* get book by isbn */
    public BookDTO getBookByIsbn(String isbn){
        Optional<BookEntity> optionalBook = bookRepository.findById(isbn);
        if (optionalBook.isEmpty())
            throw  new ResourceNotFoundException("Book not found with ISBN:"+ isbn);
        return convertToDTO(optionalBook.get());

    }

    /* get All Books in pageable from */
    public Page<BookDTO> getAllBooksInPage(int page) {
        Pageable pageable = PageRequest.of(page,6);
        Page<BookEntity> allBks = bookRepository.findAll(pageable);
        return  allBks.map((books)->{
            return  modelMapper.map(books,BookDTO.class);
        });
    }


}
