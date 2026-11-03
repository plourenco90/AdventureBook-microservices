package com.pictet.adventureservice.infrastructure.client.book;

import com.pictet.adventureservice.infrastructure.client.book.dto.BookPlayableResponse;
import com.pictet.adventureservice.infrastructure.client.book.exception.BookNotFoundException;
import com.pictet.adventureservice.infrastructure.client.book.exception.BookServiceUnavailableException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookClient {

    private final WebClient bookWebClient;

    @Retry(name = "bookService")
    @Bulkhead(name = "bookService")
    @CircuitBreaker(name = "bookService")
    public BookPlayableResponse getPlayableBook(Long bookId) {
        log.debug("Fetching playable book: bookId={}", bookId);
        return fetchPlayableBook(bookId);
    }

    private BookPlayableResponse fetchPlayableBook(Long bookId) {
        try {
            return bookWebClient.get()
                    .uri("/api/internal/books/{id}/playable", bookId)
                    .retrieve()
                    .onStatus(status -> status.value() == 404,
                            response -> Mono.error(new BookNotFoundException(bookId)))
                    .onStatus(HttpStatusCode::isError,
                            response -> Mono.error(new BookServiceUnavailableException(
                                    "Book Service returned HTTP " + response.statusCode().value())))
                    .bodyToMono(BookPlayableResponse.class)
                    .switchIfEmpty(Mono.error(new BookServiceUnavailableException(
                            "Book Service returned an empty response")))
                    .timeout(Duration.ofSeconds(3))
                    .block();
        } catch (BookNotFoundException | BookServiceUnavailableException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw new BookServiceUnavailableException(ex);
        }
    }
}
