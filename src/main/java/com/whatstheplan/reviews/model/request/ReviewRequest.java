package com.whatstheplan.reviews.model.request;

import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.utils.Utils;
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
public class ReviewRequest {

    @NotNull(message = "User ID cannot be null")
    private UUID userId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    @NotBlank(message = "Review text cannot be blank")
    @Size(max = 1000, message = "Review text must not exceed 1000 characters")
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
