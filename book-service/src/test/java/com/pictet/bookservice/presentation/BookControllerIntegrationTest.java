package com.pictet.bookservice.presentation;

import com.pictet.bookservice.persistence.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BookControllerIntegrationTest extends AbstractIntegrationTest {

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
