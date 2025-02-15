package com.projects.lms_server.repository;

import com.projects.lms_server.entites.BookEntity;
import com.projects.lms_server.entites.enums.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,String> {
    @Query(value = "SELECT * FROM book b WHERE b.genre LIKE %:genre%", nativeQuery = true)
    List<BookEntity> findBooksByGenre(Genre genre);
}
