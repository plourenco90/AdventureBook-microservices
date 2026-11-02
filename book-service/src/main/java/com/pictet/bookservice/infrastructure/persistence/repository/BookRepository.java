package com.pictet.bookservice.infrastructure.persistence.repository;

import com.pictet.bookservice.infrastructure.persistence.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findOneById(Long id);

}
