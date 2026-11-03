package com.pictet.bookservice.presentation;

import com.pictet.bookservice.application.dto.BookSummaryResponse;
import com.pictet.bookservice.application.dto.BookValidationResponse;
import com.pictet.bookservice.application.dto.ServiceErrorResponse;
import com.pictet.bookservice.application.dto.UnreachableSectionsResponse;
import com.pictet.bookservice.application.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/books")
@Tag(name = "Books", description = "Public book service endpoints")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Finds all books", description = "Finds all books stored in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Books Found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookSummaryResponse.class))))})
    public List<BookSummaryResponse> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets the summary of a book", description = "Gets the summary of a given book")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Details of the book",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookSummaryResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceErrorResponse.class)))})
    public BookSummaryResponse findById(@PathVariable Long id) {
        return bookService.findSummaryById(id);
    }

    @GetMapping("/{id}/validation")
    @Operation(summary = "Gets the validation status of a book and the errors", description = "Gets the validation status of a book by its ID and the errors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Book validation status and list of errors",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookValidationResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceErrorResponse.class)))})
    public BookValidationResponse validation(@PathVariable Long id) {
        return bookService.validation(id);
    }

    @GetMapping("/{id}/unreachable-sections")
    @Operation(summary = "Gets the unreachable sections of a book", description = "Gets the list of unreachable sections of a given book")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of unreachable sections of the book",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UnreachableSectionsResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceErrorResponse.class))),
            @ApiResponse(
                    responseCode = "422",
                    description = "Invalid Book",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceErrorResponse.class)))})
    public ResponseEntity<UnreachableSectionsResponse> findUnreachableSections(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findUnreachableSections(id));
    }
}