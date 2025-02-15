package com.projects.lms_server.service;

import com.projects.lms_server.dto.BookCopyDTO;
import com.projects.lms_server.dto.BookDTO;
import com.projects.lms_server.dto.RecordDTO;
import com.projects.lms_server.dto.ReservedBookDTO;
import com.projects.lms_server.entites.*;
import com.projects.lms_server.entites.enums.Condition;
import com.projects.lms_server.entites.enums.Genre;
import com.projects.lms_server.entites.enums.Status;
import com.projects.lms_server.exceptions.BookLimitExceededException;
import com.projects.lms_server.exceptions.BookNotAvailableException;
import com.projects.lms_server.exceptions.ReservationNotFoundException;
import com.projects.lms_server.exceptions.ResourceNotFoundException;
import com.projects.lms_server.repository.BookCopyRepository;
import com.projects.lms_server.repository.BookRepository;
import com.projects.lms_server.repository.RecordRepository;
import com.projects.lms_server.repository.ReservedBookRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


import static com.projects.lms_server.entites.enums.Condition.*;
import static com.projects.lms_server.entites.enums.Status.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookCopyService {

    private final ModelMapper modelMapper;
    private final BookCopyRepository bookCopyRepository;
    private final BookService bookService;
    private final RecordRepository recordRepository;
    private final UserService userService;
    private final ReservedBookRepository  reservedBookRepository;
    private final BookRepository bookRepository;


    /* converting bookCopyEntity to book dto*/
    private BookCopyDTO convertToDTO(BookCopyEntity bookCopy) {
        return modelMapper.map(bookCopy,BookCopyDTO.class);
    }

    /* Converting bookCopyDTO to bookCopy Entity*/
    private BookCopyEntity convertToEntity(BookCopyDTO bookCopyDTO){
        return modelMapper.map(bookCopyDTO,BookCopyEntity.class);
    }

    /* get All Books */
    public Page<BookCopyDTO> getAllBooks(int pageNumber) {

        // Set a fixed page size (e.g., 6)
        int pageSize = 6;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Fetch paginated unique books based on ISBN (one book copy per ISBN)
        Page<BookEntity> booksPage = bookRepository.findAll(pageable);

        // Map the list of BookEntity to BookCopyDTO
        List<BookCopyDTO> bookCopyDTOs = booksPage.getContent().stream()
                .map(book -> {
                    String isbn = book.getIsbn();

                    // Fetch the first available book copy
                    Optional<BookCopyEntity> availableBookCopy = bookCopyRepository.findFirstByBook_IsbnAndStatus(isbn, Status.AVAILABLE);

                    // If no available book copy, check for BORROWED, then RESERVED
                    if (availableBookCopy.isEmpty()) {
                        availableBookCopy = bookCopyRepository.findFirstByBook_IsbnAndStatus(isbn, Status.BORROWED);
                    }
                    if (availableBookCopy.isEmpty()) {
                        availableBookCopy = bookCopyRepository.findFirstByBook_IsbnAndStatus(isbn, Status.RESERVED);
                    }

                    // If no book copy found, check for PERPETUALLY_AVAILABLE
                    if (availableBookCopy.isEmpty()) {
                        availableBookCopy = bookCopyRepository.findFirstByBook_IsbnAndStatus(isbn, Status.PERPETUALLY_AVAILABLE);
                    }
                    if (availableBookCopy.isEmpty()) {
                        return null;
                    }

                    // Convert to DTO and set availability status
                    BookCopyDTO bookCopyDTO = convertToDTO(availableBookCopy.get());
                    bookCopyDTO.setIsAvailable(availableBookCopy.get().getStatus() == Status.AVAILABLE);

                    return bookCopyDTO;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Return the paginated results as a Page<BookCopyDTO>
        return new PageImpl<>(bookCopyDTOs, pageable, booksPage.getTotalElements());
    }


    /* search book by title*/
    public BookCopyDTO getBookByTitle(String title) {

        // Try fetching the first book which is  available and match with title

        Optional<BookCopyEntity> optionalBookCopy = bookCopyRepository.findFirstByBook_TitleAndStatus(title, Status.AVAILABLE);

        // Fallback: If no available book is found, fetch the first book by title
        BookCopyEntity bookCopy = optionalBookCopy.orElseGet(() ->
                bookCopyRepository.findFirstByBook_Title(title).orElseThrow(() ->
                        new ResourceNotFoundException("Book not found in library: " + title)
                )
        );

        // Convert the entity to a DTO
        BookCopyDTO bookCopyDTO = convertToDTO(bookCopy);

        // Set availability in the DTO
        bookCopyDTO.setIsAvailable(bookCopy.getStatus() == AVAILABLE);

        return bookCopyDTO;
    }

    /* get all books By genre */
    public List<BookCopyDTO> getAllBooksByGenre(Genre genre) {
        // Fetch all books by genre
        List<BookDTO> allBooks = bookService.getBooksByGenre(genre);
        log.info("all books found with genre {}",genre);
        // Initialize the result list
        List<BookCopyDTO> bookCopyDTOs = new ArrayList<>();

        for (BookDTO bookDTO : allBooks) {
            // Fetch the first available book copy by ISBN
            Optional<BookCopyEntity> optionalBookCopy = bookCopyRepository.findFirstByBook_IsbnAndStatus(bookDTO.getIsbn(), AVAILABLE);

            BookCopyEntity bookCopy= null;
            if (optionalBookCopy.isPresent()) {
                // Assign the available book copy
                bookCopy = optionalBookCopy.get();
            } else {
                // If no available book copy exists, fetch any book copy
                Optional<BookCopyEntity> optBookCopy = bookCopyRepository.findFirstByBook_Isbn(bookDTO.getIsbn());
                if (optBookCopy.isPresent()) {
                    bookCopy = optBookCopy.get();
                }
            }
            // Convert the book copy entity to DTO and add to the result list
            BookCopyDTO bookCopyDTO = convertToDTO(bookCopy);

            /* setting the availability of the book */
            assert bookCopy != null;
            bookCopyDTO.setIsAvailable(bookCopy.getStatus() == AVAILABLE);

            bookCopyDTOs.add(bookCopyDTO);
        }

        return bookCopyDTOs;
    }

    /* get Book by bookId and get reviews form bookEntity by isbn */
    public BookCopyDTO getBookDetailsById(String bookId) {

        /* fetching bookCopyEntity by book id , else throwing the error */
        BookCopyEntity bookCopy = bookCopyRepository.findById(bookId).orElseThrow(()->
                new ResourceNotFoundException("Book Not found with given ID:"+ bookId));

        /* convert BookCopyEntity to dto*/
        BookCopyDTO bookCopyDTO = convertToDTO(bookCopy);
        /* checking the availability of the books*/
        bookCopyDTO.setIsAvailable(bookCopy.getStatus() == AVAILABLE);

        /* returning the book copy dto*/
        return bookCopyDTO;
    }

    /* return the borrowed books */
    @Transactional
    public RecordDTO returnBook(String bookId, Condition condition) {
        log.info("Book id {}, condition:{}",bookId,condition);
        /* fetching the book by their id*/
        BookCopyEntity book = bookCopyRepository.findById(bookId).orElseThrow(()->
                new ResourceNotFoundException("Invalid Book Id:"+ bookId));
        log.info("consistent result ");
        /* make sure that book is borrowed by the user */
        if (book.getStatus() != BORROWED)
            throw  new ResourceNotFoundException("No Pending  Record found with Book ID:"+ bookId);

        /* fetching the record of current book which a user borrowed */
        RecordEntity record = recordRepository.findByBookCopy_BookIdAndStatus(bookId,BORROWED).orElseThrow(()->
                new ResourceNotFoundException("No record Found !,Book ID:"+ bookId));

        /* calculate the fine */
        long  days =  ChronoUnit.DAYS.between(record.getBorrowDate(), LocalDateTime.now());
        long fine = Math.max(0, days-30);

        /*if book is damaged then fare the book price */
        if ( condition == DAMAGED || condition == LOST)
            fine += book.getBook().getPrice();

       /* Mark current book is available based on the condition */
        if ( condition == GOOD) {
            log.info("set as available");
            book.setStatus(AVAILABLE);
            book.setCondition(GOOD);
        } else {
            book.setStatus(ARCHIVED);
            if (condition == Condition.LOST)
                book.setCondition(LOST);
            else
                book.setCondition(DAMAGED);
        log.info("condition {}",condition);

        }
        /* saved the book with updated details */
        bookCopyRepository.save(book);

        /* Mark this record as the complete */
        record.setReturnDate(LocalDate.now());
        record.setStatus(RETURNED);
        record.setFine((int) fine);
        record.setCondition(condition);
        /* get the current Admin form the security context*/
        UserEntity admin = userService.getCurrentUser();
        record.setReturnedBy(admin);

        /* saved the record into the DB*/
       RecordEntity savedRecord = recordRepository.save(record);

       /* return the record */
        log.info("returned successfully");
       return  modelMapper.map(savedRecord,RecordDTO.class);
    }

    /* Reserved Book By the User */
    @Transactional
    public ReservedBookDTO reservedBook(String bookId) {
        // Fetch the book
        BookCopyEntity book = bookCopyRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Book ID: " + bookId));
        log.info("book copy is {}",book);
        // Check if the book is available
        if (book.getStatus() != Status.AVAILABLE)
            throw new BookNotAvailableException("Book is not available!");


        // Get the current user from the security context
        UserEntity member = userService.getCurrentUser();

        log.info("i m here after member");

        // Count borrowed books that are not yet returned
        long borrowedCount = recordRepository.countByUser_UserIdAndStatusAndReturnDateIsNull(member.getUserId(), Status.BORROWED);

        // Count reserved books
        long reservedBooks = reservedBookRepository.countByUserId(member.getUserId());

        // Calculate book limit left
        long bookLimitLeft = 5 - borrowedCount - reservedBooks;

        // If the book limit is exceeded, throw an error
        if (bookLimitLeft <= 0)
            throw new BookLimitExceededException("Book limit exceeded!");

        // Update the book's status to RESERVED
        book.setStatus(Status.RESERVED);
        book.setBorrower(member);
        bookCopyRepository.save(book);
        log.info("user Service saved the BookCopy Status,{}",book);

        // Create and save the reserved book entity
        ReservedBookEntity toBeReserved = new ReservedBookEntity();
        toBeReserved.setBookCopy(book);
        toBeReserved.setUser(member);
        toBeReserved.setStatus(RESERVED);
        toBeReserved.setReservationDate(LocalDate.now());
        toBeReserved.setImage(book.getImage());
        ReservedBookEntity reservedBook = reservedBookRepository.save(toBeReserved);
        log.info("saved the reserved book entity ,{}",reservedBook);
        // Return ReservedBookDTO
        return modelMapper.map(reservedBook, ReservedBookDTO.class);
    }

    /* approve or reject the Member Book request */
    @Transactional
    public RecordDTO approveReservedBook(String id,Status status) {

        /* get the reserved book entity*/
        ReservedBookEntity reservedBook = reservedBookRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Invalid ReserveBook Id:" + id));
        /*
         * Get the requested book member (UserEntity).
         * This is the user who reserved the book.
         */
        UserEntity member = reservedBook.getUser();


        /* Fetch the current admin (UserEntity) */
        UserEntity admin = userService.getCurrentUser();

        /*
         * Fetch the book copy from the reserved book entity.
         * We need to check the current status to ensure it can be approved.
         */
        BookCopyEntity bookCopy = reservedBook.getBookCopy();

        /* admin rejected the request */
        if (status == Status.REJECT) {
            /*
             * If the status is REJECT, mark the book as AVAILABLE.
             * Delete the ReservedBookEntity to indicate the reservation is no longer active.
             */
            bookCopy.setStatus(Status.AVAILABLE);
            bookCopyRepository.save(bookCopy);
            reservedBookRepository.delete(reservedBook);
            return new RecordDTO();
        }
        if ( !member.getIsActive())
            throw  new IllegalStateException("Member is not active");
        /*
         * Ensure the book is in RESERVED status before proceeding.
         */
        if (bookCopy.getStatus() != Status.RESERVED) {
            throw new IllegalStateException("The book is not in reserved state and cannot be approved.");
        }
        /*
         * Update the status of the book copy .
         * This marks the book as issued to the user.
         */
        bookCopy.setStatus(Status.BORROWED);
        bookCopyRepository.save(bookCopy);

        /*
         * Create a new RecordEntity for the borrowing transaction.
         * The record will link the user, the admin (who issued the book),
         * and the specific book copy that is being borrowed.
         */
        RecordEntity record = new RecordEntity();
        record.setUser(member);
        record.setIssuedBy(admin);
        record.setBookCopy(bookCopy);
        record.setStatus(BORROWED);
        record.setIsbn(bookCopy.getBook().getIsbn());
        RecordEntity savedRecord = recordRepository.save(record);
        /*
         * After issuing the book, delete the reserved book entry.
         * This indicates that the reservation has been completed and is no longer needed.
         */
        reservedBookRepository.delete(reservedBook);

        RecordDTO recordDTO =  modelMapper.map(savedRecord, RecordDTO.class);
        recordDTO.setBookTitle(savedRecord.getBookCopy().getBook().getTitle());
        recordDTO.setMemberName(savedRecord.getUser().getName());
        recordDTO.setIssuedBy(admin.getUserId());
        log.info("dto{}",recordDTO);
        return recordDTO;

    }

    /* fetch all reserved books by a Member */
    public List<ReservedBookDTO> getAllReservedBooks(String id ) {
        /* get the Current Member*/
        UserEntity member = userService.getCurrentUser();
        String userId = id.equals("NA")? member.getUserId():id;
        List<ReservedBookEntity> reservedBooks = reservedBookRepository.findAllByUser_UserId(userId);
        return reservedBooks.stream()
                .map(reservedBookEntity -> modelMapper.map(reservedBookEntity, ReservedBookDTO.class))
                .toList();
    }

    /* get All reserved Books by all the member of Library */
    public Page<ReservedBookDTO> getAllReservedBooks(int page) {
        /* create the page*/
        Pageable pageable = PageRequest.of(page,5);

        /* fetch the all reserved books */
        Page<ReservedBookEntity> reservedBooks = reservedBookRepository.findAll(pageable);

        /* map with dto and return */
        return  reservedBooks.map(book->{
            ReservedBookDTO resBook = modelMapper.map(book,ReservedBookDTO.class);
            resBook.setBookTitle(book.getBookCopy().getBook().getTitle());
            resBook.setMemberName(book.getUser().getName());
            resBook.setImage(book.getImage());
            return resBook;
        });
    }

    @Transactional
    public ReservedBookDTO deleteReservedBook(String id) {

        ReservedBookEntity reservedBook = reservedBookRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Invalid ReservedBook Id:"+ id));

        /* fetch the book Copy */
        BookCopyEntity bookCopy = reservedBook.getBookCopy();
        /* set book is available and saved into db*/
        bookCopy.setStatus(AVAILABLE);
        bookCopyRepository.save(bookCopy);

        /* set status as Available*/
        reservedBook.setStatus(AVAILABLE);

        /* delete the reservedBook from db*/
        reservedBookRepository.delete(reservedBook);
        return modelMapper.map(reservedBook,ReservedBookDTO.class);
    }

    public Page<RecordDTO> getAllDamagedBooks(int page) {
        Pageable pageable = PageRequest.of(page,3);
        Set<Condition> st =  new HashSet<>();
        st.add(LOST);
        st.add(DAMAGED);
        Page<RecordEntity> records = recordRepository.findByConditionIn(st,pageable) ;
        return records.map((rec)->{
            RecordDTO recc = modelMapper.map(rec,RecordDTO.class);
            recc.setBookTitle(rec.getBookCopy().getBook().getTitle());
            recc.setStatus(rec.getStatus());
            recc.setIsbn(rec.getIsbn());
            recc.setMemberName(rec.getUser().getName());
            recc.setBookCopy(rec.getBookCopy().getBookId());
            recc.setFine(rec.getFine());
            recc.setCondition(rec.getCondition());
            recc.setIssuedBy(rec.getIssuedBy().getUserId());
            recc.setReturnedBy(rec.getReturnedBy().getUserId());
            recc.setUserId(rec.getUser().getUserId());
            return recc;
        });
    }
}
