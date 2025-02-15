package com.projects.lms_server.repository;

import com.projects.lms_server.entites.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity,String> {
    Optional<AuthorEntity> findByEmail(String email);
}
