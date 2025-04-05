package com.whatstheplan.reviews.service;

import com.whatstheplan.reviews.client.user.UserClient;
import com.whatstheplan.reviews.exceptions.ReviewNotExistsException;
import com.whatstheplan.reviews.exceptions.ReviewRaterMismatchException;
import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.model.request.ReviewRequest;
import com.whatstheplan.reviews.model.response.ReviewResponse;
import com.whatstheplan.reviews.repository.ReviewRepository;
import com.whatstheplan.reviews.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserClient userClient;

    public ReviewResponse createReview(ReviewRequest request) {
        log.info("Saving new review: {}", request);
        Review review = reviewRepository.save(request.toEntity());
        return generateReviewResponse(review);
    }

    public ReviewResponse getReviewById(UUID id) {
        log.info("Finding review with id: {}", id);
        return reviewRepository.findById(id)
                .map(this::generateReviewResponse)
                .orElseThrow(() -> new ReviewNotExistsException("Review not found with id: " + id));
    }

    public List<ReviewResponse> getReviewsByUserId(UUID userId) {
        log.info("Finding all reviews from user: {}", userId);
        return reviewRepository.findByUserId(userId).stream()
                .map(this::generateReviewResponse)
                .toList();
    }

    public void deleteReview(UUID id) {
        log.info("Deleting review with id: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotExistsException("Review not found with id: " + id));

        if (!Utils.getUserId().equals(review.getRaterId())) {
            throw new ReviewRaterMismatchException("User %s does not own review %s"
                    .formatted(Utils.getUserId(), review.getId()));
        }

        reviewRepository.deleteById(id);
    }

    private ReviewResponse generateReviewResponse(Review review) {
        return ReviewResponse.from(
                review,
                userClient.getUserBasicInfo(review.getRaterId()).getUsername(),
                userClient.getUserBasicInfo(review.getUserId()).getUsername()
        );
    }
}
