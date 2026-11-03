package com.pictet.bookservice.application.service;

import com.pictet.bookservice.application.dto.BookResponse;
import com.pictet.bookservice.application.dto.BookSummaryResponse;
import com.pictet.bookservice.application.dto.BookValidationResponse;
import com.pictet.bookservice.application.dto.UnreachableSectionsResponse;
import com.pictet.bookservice.application.mapper.BookResponseMapper;
import com.pictet.bookservice.domain.exception.BookNotFoundException;
import com.pictet.bookservice.domain.exception.InvalidBookException;
import com.pictet.bookservice.domain.model.Book;
import com.pictet.bookservice.domain.unreachable.UnreachableSectionsAnalyzer;
import com.pictet.bookservice.infrastructure.persistence.entity.BookValidationEntity;
import com.pictet.bookservice.infrastructure.persistence.mapper.BookPersistenceMapper;
import com.pictet.bookservice.infrastructure.persistence.repository.BookRepository;
import com.pictet.bookservice.infrastructure.persistence.repository.BookValidationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookPersistenceMapper bookMapper;
    private final BookResponseMapper bookResponseMapper;
    private final BookValidationRepository bookValidationRepository;
    private final UnreachableSectionsAnalyzer unreachableSectionsAnalyzer = new UnreachableSectionsAnalyzer();

    @Transactional(readOnly = true)
    public List<BookSummaryResponse> findAll() {
        return bookRepository.findAll().stream()
                .map(book -> new BookSummaryResponse(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        isValid(book.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public BookSummaryResponse findSummaryById(Long id) {
        return bookRepository.findOneById(id).map(book ->
                new BookSummaryResponse(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        isValid(book.getId()))).orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        log.info("Finding book with ID={}", id);
        Book book = findBook(id);
        return bookResponseMapper.toResponse(book);
    }

    @Transactional(readOnly = true)
    public BookValidationResponse validation(Long id) {
        Book book = findBook(id);
        Optional<BookValidationEntity> bookValidation = bookValidationRepository.findOneByBookId(book.id());
        if (bookValidation.isPresent() && !bookValidation.get().getErrors().isEmpty()) {
            return new BookValidationResponse(
                    book.id(),
                    false,
                    bookResponseMapper.toResponse(bookValidation.get().getErrors())
            );
        }
        return new BookValidationResponse(book.id(), true, new ArrayList<>());
    }

    @Transactional(readOnly = true)
    public UnreachableSectionsResponse findUnreachableSections(Long id) {
        Book book = findBook(id);
        BookValidationResponse validationResult = validation(id);
        if (!validationResult.valid()) {
            throw new InvalidBookException(id);
        }
        List<Integer> unreachableSectionIds = unreachableSectionsAnalyzer.findUnreachableSections(book);
        return new UnreachableSectionsResponse(book.id(), unreachableSectionIds);
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDomain)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    private boolean isValid(Long bookId) {
        final Optional<BookValidationEntity> validation = bookValidationRepository.findOneByBookId(bookId);
        return validation.isEmpty() || validation.get().getErrors().isEmpty();
    }
}