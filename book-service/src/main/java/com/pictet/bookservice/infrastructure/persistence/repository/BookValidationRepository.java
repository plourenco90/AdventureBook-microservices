package com.pictet.bookservice.infrastructure.persistence.repository;

import com.pictet.bookservice.infrastructure.persistence.entity.BookEntity;
import com.pictet.bookservice.infrastructure.persistence.entity.BookValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookValidationRepository extends JpaRepository<BookValidationEntity, Long> {

    Optional<BookValidationEntity> findOneByBookId(Long bookId);

    void deleteAllByBook(BookEntity book);
}
