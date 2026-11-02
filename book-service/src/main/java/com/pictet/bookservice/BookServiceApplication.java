package com.pictet.bookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BookServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(BookServiceApplication.class, args);
    }
}
