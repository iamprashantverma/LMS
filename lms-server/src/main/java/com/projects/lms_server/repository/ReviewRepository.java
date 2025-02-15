package com.projects.lms_server.repository;

import com.projects.lms_server.entites.BookEntity;
import com.projects.lms_server.entites.ReviewEntity;
import com.projects.lms_server.entites.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,String> {
    
    Optional<ReviewEntity> findByUser_UserIdAndBook_Isbn(String userId, String bookIsbn);

    Page<ReviewEntity> findAllByBook(BookEntity book, Pageable pageable);
}
