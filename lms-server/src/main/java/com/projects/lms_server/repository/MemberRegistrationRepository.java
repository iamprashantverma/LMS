package com.projects.lms_server.repository;

import com.projects.lms_server.entites.MemberRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRegistrationRepository extends JpaRepository<MemberRegistrationEntity , String> {

    Optional<MemberRegistrationEntity> findByEmail(String email);
}
