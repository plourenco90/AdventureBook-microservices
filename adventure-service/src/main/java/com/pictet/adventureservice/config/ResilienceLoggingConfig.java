package com.pictet.adventureservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ResilienceLoggingConfig {

    @Bean
    ApplicationRunner resilienceLogging(RetryRegistry retryRegistry, CircuitBreakerRegistry circuitBreakerRegistry) {
        return args -> {
            Retry retry = retryRegistry.retry("bookService");
            retry.getEventPublisher()
                    .onRetry(event -> log.warn("Book Service retry: attempt={}, cause={}",
                            event.getNumberOfRetryAttempts(),
                            event.getLastThrowable().toString()));
            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("bookService");
            circuitBreaker.getEventPublisher()
                    .onStateTransition(event ->
                            log.warn("Book Service circuit breaker: {}", event.getStateTransition()));
        };
    }
}
