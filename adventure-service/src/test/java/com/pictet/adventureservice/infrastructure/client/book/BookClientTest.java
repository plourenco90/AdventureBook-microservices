package com.pictet.adventureservice.infrastructure.client.book;

import com.pictet.adventureservice.infrastructure.client.book.dto.BookPlayableResponse;
import com.pictet.adventureservice.infrastructure.client.book.exception.BookNotFoundException;
import com.pictet.adventureservice.infrastructure.client.book.exception.BookServiceUnavailableException;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class BookClientTest {
    HttpServer server;
    AtomicInteger status = new AtomicInteger(200);
    AtomicInteger requests = new AtomicInteger();
    String body = "{\"valid\":true,\"book\":{\"id\":1,\"title\":\"Demo\",\"author\":\"A\",\"sections\":[]}}";
    BookClient client;

    @BeforeEach
    void setup() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/api/internal/books/1/playable", exchange -> {
            requests.incrementAndGet();
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            int code = status.get();
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(code, code == 204 ? -1 : bytes.length);
            if (code != 204) try (var out = exchange.getResponseBody()) {
                out.write(bytes);
            }
            else exchange.close();
        });
        server.start();
        client = new BookClient(WebClient.builder()
                .baseUrl("http://127.0.0.1:" + server.getAddress().getPort())
                .build());
    }

    @AfterEach
    void stop() {
        server.stop(0);
    }

    @Test
    void successfulResponseIsDeserialized() {
        BookPlayableResponse result = client.getPlayableBook(1L);
        assertTrue(result.valid());
        assertEquals(1L, result.book().id());
        assertEquals(1, requests.get());
    }

    @Test
    void notFoundIsPreserved() {
        status.set(404);
        assertThrows(BookNotFoundException.class, () -> client.getPlayableBook(1L));
    }

    @Test
    void serverErrorBecomesUnavailable() {
        status.set(503);
        assertThrows(BookServiceUnavailableException.class, () -> client.getPlayableBook(1L));
    }

    @Test
    void emptyResponseBecomesUnavailable() {
        status.set(204);
        assertThrows(BookServiceUnavailableException.class, () -> client.getPlayableBook(1L));
    }
}
