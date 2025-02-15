package com.projects.lms_server.repository;

import com.projects.lms_server.entites.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository  extends JpaRepository<PublisherEntity,String> {
    Optional<PublisherEntity> findByEmail(String email);
}
