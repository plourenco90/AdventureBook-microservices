package com.pictet.bookservice.presentation;


import com.pictet.bookservice.application.dto.ServiceErrorResponse;
import com.pictet.bookservice.domain.exception.BookNotFoundException;
import com.pictet.bookservice.domain.exception.InvalidBookException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BookServiceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ServiceErrorResponse> handleBookNotFound(BookNotFoundException exception) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse(HttpStatus.NOT_FOUND.name(),
                exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<ServiceErrorResponse> handleInvalidBook(InvalidBookException exception) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse(HttpStatus.UNPROCESSABLE_CONTENT.name(),
                exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unexpected error processing request", ex);
        return ResponseEntity.internalServerError().body(new ServiceErrorResponse("UNEXPECTED_ERROR", ex.getMessage()));
    }
}
