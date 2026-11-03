package com.pictet.adventureservice.presentation;

import com.pictet.adventureservice.application.dto.ServiceErrorResponse;
import com.pictet.adventureservice.domain.exception.AdventureAlreadyFinishedException;
import com.pictet.adventureservice.domain.exception.AdventureNotFoundException;
import com.pictet.adventureservice.domain.exception.InvalidOptionException;
import com.pictet.adventureservice.domain.exception.SectionNotFoundException;
import com.pictet.adventureservice.infrastructure.client.book.exception.BookNotFoundException;
import com.pictet.adventureservice.infrastructure.client.book.exception.BookServiceUnavailableException;
import com.pictet.adventureservice.infrastructure.client.book.exception.InvalidBookException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AdventureServiceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<ServiceErrorResponse> invalidBookException(InvalidBookException e) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse("INVALID_BOOK", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ServiceErrorResponse> bookNotFoundException(BookNotFoundException e) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse("INVALID_BOOK", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler(AdventureAlreadyFinishedException.class)
    public ResponseEntity<ServiceErrorResponse> adventureAlreadyFinishedException(AdventureAlreadyFinishedException e) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse("INVALID_STATE", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServiceErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Object[] errors = e.getDetailMessageArguments();
        ServiceErrorResponse errorResponse = new ServiceErrorResponse("INVALID_ARGUMENT",
                String.valueOf(errors[errors.length - 1]));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler(AdventureNotFoundException.class)
    public ResponseEntity<ServiceErrorResponse> adventureNotFoundException(AdventureNotFoundException e) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse("NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler(SectionNotFoundException.class)
    public ResponseEntity<ServiceErrorResponse> sectionNotFoundException(SectionNotFoundException e) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse("NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler(InvalidOptionException.class)
    public ResponseEntity<ServiceErrorResponse> invalidOptionException(InvalidOptionException e) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse("INVALID_OPTION", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler(BookServiceUnavailableException.class)
    public ResponseEntity<ServiceErrorResponse> bookServiceUnavailableException(BookServiceUnavailableException e) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse("BAD_GATEWAY", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler({ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class})
    public ResponseEntity<ServiceErrorResponse> handleConcurrentUpdate(Exception exception) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse(HttpStatus.CONFLICT.name(),
                exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unexpected error processing request", ex);
        return ResponseEntity.internalServerError().body(new ServiceErrorResponse("UNEXPECTED_ERROR", ex.getMessage()));
    }
}
