package com.projects.lms_server.repository;


import com.projects.lms_server.entites.RecordEntity;
import com.projects.lms_server.entites.enums.Condition;
import com.projects.lms_server.entites.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity,Long> {

    Optional<RecordEntity> findByBookCopy_BookIdAndStatus(String bookId, Status status);

    long countByUser_UserIdAndStatusAndReturnDateIsNull(String userId, Status status);

    List<RecordEntity> findByUser_UserIdAndReturnDateIsNull(String id);

    
    Page<RecordEntity> findByBorrowDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<RecordEntity> findByBorrowDateBetweenAndReturnDateIsNull(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<RecordEntity> findByReturnDateIsNull(Pageable pageable);


    Page<RecordEntity> findByReturnDateIsNullAndBorrowDateBefore(LocalDate thirtyDaysAgo, Pageable pageable);


    Page<RecordEntity> findByIsbnAndReturnDateNull(String isbn, Pageable pageable);

    @Query("SELECT r FROM RecordEntity r WHERE r.condition IN :st")
    Page<RecordEntity> findByConditionIn(Set<Condition> st, Pageable pageable);

}
