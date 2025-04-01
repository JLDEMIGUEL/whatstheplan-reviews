package com.whatstheplan.reviews.controller;

import com.whatstheplan.reviews.model.request.ReviewRequest;
import com.whatstheplan.reviews.model.response.ReviewResponse;
import com.whatstheplan.reviews.service.ReviewService;
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
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request) {
        log.info("Creating new review with data: {}", request);

        ReviewResponse savedReview = reviewService.createReview(request);

        log.info("Successfully created review with ID: {}", savedReview.getId());
        return ResponseEntity.status(CREATED).body(savedReview);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable UUID id) {
        log.info("Retrieving review by ID: {}", id);

        ReviewResponse review = reviewService.getReviewById(id);

        log.info("Successfully retrieved review: {}", id);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable UUID userId) {
        log.info("Retrieving all reviews for user: {}", userId);

        List<ReviewResponse> reviews = reviewService.getReviewsByUserId(userId);

        log.info("Successfully retrieved {} reviews for user: {}", reviews.size(), userId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        log.info("Deleting review with ID: {}", id);

        reviewService.deleteReview(id);

        log.info("Successfully deleted review with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}

