package com.projects.lms_server.controller;

import com.projects.lms_server.dto.BookCopyDTO;
import com.projects.lms_server.dto.RecordDTO;
import com.projects.lms_server.dto.ReservedBookDTO;
import com.projects.lms_server.dto.ReviewDTO;
import com.projects.lms_server.entites.enums.Genre;
import com.projects.lms_server.service.BookCopyService;
import com.projects.lms_server.service.RecordService;
import com.projects.lms_server.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    /* injecting the all dependencies related to member */
    private  final BookCopyService bookCopyService;
    private final ReviewService reviewService;
    private final RecordService recordService;

    /* Books */
    /* get all books */
    @GetMapping("/book")
    public ResponseEntity<Page<BookCopyDTO>> getAllBooks(@RequestParam(required = false,defaultValue = "0")int page) {
        Page<BookCopyDTO> books = bookCopyService.getAllBooks(page);
        return  ResponseEntity.ok(books);
    }

    /* get book details by id*/
    @GetMapping("g-book-id/{id}")
    public ResponseEntity<BookCopyDTO> getBookDetailsById(@PathVariable String id) {
        BookCopyDTO bookCopyDTO = bookCopyService.getBookDetailsById(id);
        return ResponseEntity.ok(bookCopyDTO);
    }

    /* get book by title */
    @GetMapping("/book/{title}")
    public ResponseEntity<BookCopyDTO> getBookByTitle(@PathVariable String title) {
        BookCopyDTO bookCopyDTO = bookCopyService.getBookByTitle(title);
        return ResponseEntity.ok(bookCopyDTO);
    }

    /* get books by Genre */
    @GetMapping("/g-book-genre/{id}")
    public ResponseEntity<List<BookCopyDTO>> getAllBooksByGenre (@PathVariable int id) {
        Genre genre = Genre.values()[id];
        log.info("Genre is {}",genre);
        List<BookCopyDTO> books = bookCopyService.getAllBooksByGenre(genre);
        return ResponseEntity.ok(books);
    }

    /* reserved a book for the user */
    @PostMapping("/reserve/{bookId}")
    public ResponseEntity<ReservedBookDTO> reservedBook(@PathVariable String bookId) {
        ReservedBookDTO reservedBook = bookCopyService.reservedBook(bookId);
        return ResponseEntity.ok(reservedBook);
    }

    /*  get single  Member all  Reserved book */
    @GetMapping("/reserve")
    private ResponseEntity<List<ReservedBookDTO>> getReservedBook() {
        List<ReservedBookDTO> reservedBooks = bookCopyService.getAllReservedBooks("NA");
        return ResponseEntity.ok(reservedBooks);
    }

    /* delete reserved Book*/
    @DeleteMapping("/reserve/{id}")
    public ResponseEntity<ReservedBookDTO> deleteReserveBookDTO(@PathVariable String id) {
        /* passing the reservation id*/
        ReservedBookDTO reservedBookDTO = bookCopyService.deleteReservedBook(id);
        return ResponseEntity.ok(reservedBookDTO);
    }

    /* give the review and comment on book*/
    @PostMapping("/review")
    private ResponseEntity<ReviewDTO> review(@RequestBody ReviewDTO review  ) {
        ReviewDTO reviewDTO = reviewService.giveReview(review);
        return ResponseEntity.ok(reviewDTO);
    }

   /* Delete the review */
    @DeleteMapping("/review/{id}")
    private ResponseEntity<ReviewDTO> deleteTheReview(@PathVariable String id) {
        ReviewDTO reviewDTO1 = reviewService.deleteReview(id);
        return ResponseEntity.ok(reviewDTO1);
    }

    /* get All review of a book */
    @GetMapping("/review/{id}")
    private ResponseEntity<Page<ReviewDTO>> getAllReviewOfBooks(@PathVariable String id,@RequestParam(defaultValue = "0",required = false) int page )  {
        Page<ReviewDTO> allReviews = reviewService.getAllReviewOfBooks(id,page);
        return ResponseEntity.ok(allReviews);
    }

    /* get member borrowed books*/
    @GetMapping("/mem-borrow-rec")
    public ResponseEntity<List<RecordDTO>> getMemberBorrowedBooks(){
        List<RecordDTO> records  = recordService.getMemberBorrowedBooks("NA");
        return ResponseEntity.ok(records);

    }


}
