package com.whatstheplan.reviews.model.response;

import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.utils.Utils;
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
public class ReviewResponse {

    private UUID id;
    private String raterUsername;
    private String username;
    private Integer rating;
    private String text;
    private Instant createdAt;
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