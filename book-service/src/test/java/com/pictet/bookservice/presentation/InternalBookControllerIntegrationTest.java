package com.pictet.bookservice.presentation;

import com.pictet.bookservice.persistence.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InternalBookControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void anonymousUserCannotReadInternalBook() throws Exception {
        mockMvc.perform(get("/internal/books/1/playable")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void tokenWithoutScopeCannotReadInternalBook() throws Exception {
        mockMvc.perform(get("/internal/books/1/playable").with(jwt())
        ).andExpect(status().isForbidden());
    }

    @Test
    void tokenWithScopeCanReadInternalBook() throws Exception {
        mockMvc.perform(get("/internal/books/1/playable")
                .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_books.internal.read")))
        ).andExpect(status().isOk());
    }

}
