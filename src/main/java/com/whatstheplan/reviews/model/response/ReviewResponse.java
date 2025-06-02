package com.whatstheplan.reviews.model.response;

import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.utils.Utils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response model for a review")
public class ReviewResponse {

    @Schema(description = "Unique identifier of the review", example = "6fa459ea-ee8a-3ca4-894e-db77e160355e")
    private UUID id;

    @Schema(description = "Username of the user who wrote the review", example = "jane_doe")
    private String raterUsername;

    @Schema(description = "Username of the user who is being reviewed", example = "john_smith")
    private String username;

    @Schema(description = "Rating score from 1 to 5", example = "4")
    private Integer rating;

    @Schema(description = "Text content of the review", example = "Great event with excellent organization.")
    private String text;

    @Schema(description = "Timestamp when the review was created", example = "2025-06-01T12:34:56Z")
    private Instant createdAt;

    @Schema(description = "Indicates if the current authenticated user is the owner of this review", example = "true")
    private Boolean isOwnedByUser;

    public static ReviewResponse from(Review entity, String raterUsername, String username) {
        return ReviewResponse.builder()
                .id(entity.getId())
                .raterUsername(raterUsername)
                .username(username)
                .rating(entity.getRating())
                .text(entity.getText())
                .createdAt(entity.getCreatedAt())
                .isOwnedByUser(Utils.getUserId().equals(entity.getRaterId()))
                .build();
    }
}