package com.pictet.bookservice.presentation;

import com.pictet.bookservice.application.dto.BookPlayableResponse;
import com.pictet.bookservice.application.dto.BookResponse;
import com.pictet.bookservice.application.dto.BookValidationResponse;
import com.pictet.bookservice.application.dto.ServiceErrorResponse;
import com.pictet.bookservice.application.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/books")
@Tag(name = "Internal Books", description = "Book Controller for internal applications")
public class InternalBookController {

    private final BookService bookService;

    @GetMapping("/{id}/playable")
    @SecurityRequirement(name = "keycloak_client_credentials")
    @Operation(summary = "Gets the status and details of a book", description = "Gets the status and details of a given book, including all sections and options")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status and details of the book",
                    content = @Content(schema = @Schema(implementation = BookPlayableResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book Not Found",
                    content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))})
    public ResponseEntity<BookPlayableResponse> getPlayableBook(@PathVariable Long id) {
        BookResponse book = bookService.findById(id);
        BookValidationResponse validation = bookService.validation(id);
        return ResponseEntity.ok(new BookPlayableResponse(validation.valid(), book));
    }
}
