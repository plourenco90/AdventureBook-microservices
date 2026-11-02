package com.pictet.bookservice.presentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldListBooks() throws Exception {
        mockMvc.perform(get("/books")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldReturnNotFoundForUnknownBook() throws Exception {
        mockMvc.perform(get("/books/99999")).andExpect(status().isNotFound());
    }

    @Test
    void shouldValidateBook() throws Exception {
        mockMvc.perform(get("/books/1/validation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }
}
