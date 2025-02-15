package com.projects.lms_server.repository;

import com.projects.lms_server.entites.ReservedBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservedBookRepository extends JpaRepository<ReservedBookEntity,String> {
    @Query("SELECT COUNT(rb) FROM ReservedBookEntity rb WHERE rb.user.userId = :userId")
    long countByUserId( String userId);

    List<ReservedBookEntity> findAllByUser_UserId(String userId);
}
