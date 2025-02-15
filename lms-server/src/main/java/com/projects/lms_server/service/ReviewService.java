package com.projects.lms_server.service;

import com.projects.lms_server.dto.ReviewDTO;
import com.projects.lms_server.entites.BookEntity;
import com.projects.lms_server.entites.ReviewEntity;
import com.projects.lms_server.entites.UserEntity;
import com.projects.lms_server.exceptions.ResourceNotFoundException;
import com.projects.lms_server.repository.BookRepository;
import com.projects.lms_server.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final BookRepository bookRepository;


    /* add the Review to a book */
    @Transactional
    public ReviewDTO giveReview(ReviewDTO review) {
        /* get the book isbn from the review dto*/
        String isbn = review.getBookId();

        /* fetch the particular book by isbn*/
        BookEntity book = bookRepository.findById(isbn).orElseThrow(()->
                new ResourceNotFoundException("Invalid ISBN :"+ isbn));

        /* fetch the current member from the Security Context Holder*/
        UserEntity member = userService.getCurrentUser();

        /* check if member already give the review of this book */
        Optional<ReviewEntity> optionalReview = reviewRepository.findByUser_UserIdAndBook_Isbn(member.getUserId(), book.getIsbn());

        /* Member Review */
        ReviewEntity reviewEntity ;
        /* if review exist  then get else create new Review */
        reviewEntity = optionalReview.orElseGet(ReviewEntity::new);

        /* set the member review about this book */
        reviewEntity.setReviewDate(LocalDate.now());
        reviewEntity.setUser(member);
        reviewEntity.setComment(review.getComment());
        reviewEntity.setRating(review.getRating());
        reviewEntity.setBook(book);

        /* save the review into the database */
        ReviewEntity savedReview = reviewRepository.save(reviewEntity);

        /* saved this review into the book*/
//        Hibernate.initialize(book.getReviews());
        log.info("book review size after initialization: {}", book);

//        book.getReviews().add(savedReview);
        log.info("review is added to book{}",book);
        /* saved the book */
        bookRepository.save(book);
        log.info("book is saved");
        log.info("member is {}",member);
        /* return the current review after converting into the dto*/
        return  modelMapper.map(savedReview,ReviewDTO.class);

    }

    /* Delete the review */
    @Transactional
    public ReviewDTO deleteReview(String id) {
        /* fetch the review */
        ReviewEntity reviewEntity  = reviewRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Invalid Review ID:"+ id));
        /* delete the review */
        reviewRepository.delete(reviewEntity);
        return modelMapper.map(reviewEntity,ReviewDTO.class);
    }


    /* get all the review of the book */
    public Page<ReviewDTO> getAllReviewOfBooks(String id, int page) {
        Pageable pageable = PageRequest.of(page,5);
        /* find the actual book */
        BookEntity book = bookRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Book Not Found"));

        /*find all the review of this book */
        Page<ReviewEntity> reviews = reviewRepository.findAllByBook(book,pageable);

        /* convert in to the dto*/
        return reviews.map(rev->{
            return modelMapper.map(rev,ReviewDTO.class);
        });
    }



}
