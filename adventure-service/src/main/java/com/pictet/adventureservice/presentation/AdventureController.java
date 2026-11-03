package com.pictet.adventureservice.presentation;


import com.pictet.adventureservice.application.dto.AdventureStateResponse;
import com.pictet.adventureservice.application.dto.MakeChoiceRequest;
import com.pictet.adventureservice.application.dto.ServiceErrorResponse;
import com.pictet.adventureservice.application.dto.StartAdventureRequest;
import com.pictet.adventureservice.application.service.AdventureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/adventures")
@Tag(name = "Adventure", description = "The adventure controller")
public class AdventureController {

    private final AdventureService adventureService;

    @PostMapping
    @Operation(summary = "Starts an adventure", description = "Starts the adventure for the player")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Adventure Started",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdventureStateResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid Book",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceErrorResponse.class)))
    })
    public ResponseEntity<AdventureStateResponse> start(@Valid @RequestBody StartAdventureRequest request) {
        AdventureStateResponse response = adventureService.start(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets the state of the adventure", description = "Gets the current state for the given adventure ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Adventure State",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdventureStateResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid Book",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Adventure Not Found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceErrorResponse.class)))})
    public ResponseEntity<AdventureStateResponse> getState(@PathVariable Long id) {
        return ResponseEntity.ok(adventureService.getState(id));
    }

    @PostMapping("/{id}/choices")
    @Operation(summary = "Makes a choice", description = "Makes the selected option choice")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Choice made with success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdventureStateResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid Option",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Adventure Not Found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceErrorResponse.class)))})
    public ResponseEntity<AdventureStateResponse> makeChoice(@PathVariable Long id, @Valid @RequestBody MakeChoiceRequest request) {
        return ResponseEntity.ok(adventureService.makeChoice(id, request));
    }
}
