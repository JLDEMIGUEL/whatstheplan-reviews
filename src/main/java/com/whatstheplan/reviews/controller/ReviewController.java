package com.whatstheplan.reviews.controller;

import com.whatstheplan.reviews.model.request.ReviewRequest;
import com.whatstheplan.reviews.model.response.ReviewResponse;
import com.whatstheplan.reviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
@Tag(name = "Reviews", description = "Operations related to event reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Create a new review",
            description = "Creates a review for an event organizer with rating and comments.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Review created successfully",
                    content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @Parameter(description = "Review creation request", required = true)
            @Valid @RequestBody ReviewRequest request) {
        log.info("Creating new review with data: {}", request);

        ReviewResponse savedReview = reviewService.createReview(request);

        log.info("Successfully created review with ID: {}", savedReview.getId());
        return ResponseEntity.status(CREATED).body(savedReview);
    }

    @Operation(summary = "Get review by ID",
            description = "Retrieves a single review by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Review retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(
            @Parameter(description = "UUID of the review to retrieve", required = true)
            @PathVariable UUID id) {
        log.info("Retrieving review by ID: {}", id);

        ReviewResponse review = reviewService.getReviewById(id);

        log.info("Successfully retrieved review: {}", id);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Get all reviews for a specific user",
            description = "Returns all reviews written for the given user ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of reviews retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found or no reviews available", content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(
            @Parameter(description = "UUID of the user whose reviews to retrieve", required = true)
            @PathVariable UUID userId) {
        log.info("Retrieving all reviews for user: {}", userId);

        List<ReviewResponse> reviews = reviewService.getReviewsByUserId(userId);

        log.info("Successfully retrieved {} reviews for user: {}", reviews.size(), userId);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Delete a review by ID",
            description = "Deletes a review identified by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "UUID of the review to delete", required = true)
            @PathVariable UUID id) {
        log.info("Deleting review with ID: {}", id);

        reviewService.deleteReview(id);

        log.info("Successfully deleted review with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}

