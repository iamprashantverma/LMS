package com.projects.lms_server.repository;

import com.projects.lms_server.entites.UserEntity;
import com.projects.lms_server.entites.enums.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

    Optional<UserEntity> findByEmail(String email);


    Page<UserEntity> findAllUserByRole(Roles roles, Pageable pageable);
    @Query("SELECT u FROM UserEntity u WHERE u.approvedBy = :approvedBy AND u.role = :role")
    Page<UserEntity> findByApprovedByAndRole(String approvedBy, Roles role, Pageable pageable);


}
