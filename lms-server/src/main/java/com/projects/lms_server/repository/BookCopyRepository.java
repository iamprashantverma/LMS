package com.projects.lms_server.repository;

import com.projects.lms_server.entites.BookCopyEntity;
import com.projects.lms_server.entites.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopyEntity, String> {


    Integer countByBook_IsbnAndStatus(String isbn, Status status);
    Optional<BookCopyEntity> findFirstByBook_TitleAndStatus(String title, Status status);

    Optional<BookCopyEntity> findFirstByBook_Title(String title);

    Optional<BookCopyEntity> findFirstByBook_IsbnAndStatus(String isbn, Status status);

    Optional<BookCopyEntity> findFirstByBook_Isbn(String isbn);

    BookCopyEntity findByBook_IsbnAndStatus(String isbn, Status status);

    Page<BookCopyEntity> findByStatus(Pageable pageable, Status status);
}
