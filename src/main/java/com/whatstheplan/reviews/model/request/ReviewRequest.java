package com.whatstheplan.reviews.model.request;

import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.utils.Utils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request model for submitting a review")
public class ReviewRequest {

    @NotNull(message = "User ID cannot be null")
    @Schema(description = "UUID of the user being reviewed", example = "6fa459ea-ee8a-3ca4-894e-db77e160355e", required = true)
    private UUID userId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    @Schema(description = "Rating score from 1 to 5", example = "4", minimum = "1", maximum = "5", required = true)
    private Integer rating;

    @NotBlank(message = "Review text cannot be blank")
    @Size(max = 1000, message = "Review text must not exceed 1000 characters")
    @Schema(description = "Review comment text", example = "Very well organized event!", maxLength = 1000, required = true)
    private String text;

    public Review toEntity() {
        return Review.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .raterId(Utils.getUserId())
                .rating(rating)
                .text(text)
                .build();
    }
}
