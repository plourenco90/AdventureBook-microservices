package com.pictet.adventureservice.presentation;

import com.pictet.adventureservice.application.dto.AdventureStateResponse;
import com.pictet.adventureservice.application.service.AdventureService;
import com.pictet.adventureservice.domain.exception.AdventureNotFoundException;
import com.pictet.adventureservice.domain.exception.InvalidOptionException;
import com.pictet.adventureservice.domain.model.AdventureStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdventureControllerTest {
    AdventureService service = mock(AdventureService.class);
    MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new AdventureController(service))
                .setControllerAdvice(new AdventureServiceExceptionHandler())
                .build();
    }

    @Test
    void startReturnsCreated() throws Exception {
        when(service.start(any())).thenReturn(new AdventureStateResponse(7L,
                1L,
                "Pedro",
                10,
                AdventureStatus.ACTIVE,
                null,
                null));
        mvc.perform(post("/adventures").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":1,\"playerName\":\"Pedro\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void getStateReturnsNotFound() throws Exception {
        when(service.getState(99L)).thenThrow(new AdventureNotFoundException(99L));
        mvc.perform(get("/adventures/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void invalidChoiceReturnsBadRequest() throws Exception {
        when(service.makeChoice(eq(7L), any())).thenThrow(new InvalidOptionException(5));
        mvc.perform(post("/adventures/7/choices").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"optionIndex\":5}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_OPTION"));
    }
}
