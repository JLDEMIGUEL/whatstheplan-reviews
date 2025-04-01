package com.whatstheplan.reviews.testconfig.utils;

import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.model.request.ReviewRequest;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class DataUtils {

    public final static UUID RATER_ID = UUID.randomUUID();
    public final static UUID USER_ID = UUID.randomUUID();


    public static ReviewRequest generateReviewRequest() {
        return ReviewRequest.builder()
                .userId(USER_ID)
                .rating(5)
                .text("Great event!")
                .build();
    }

    public static Review generateReviewEntity(UUID raterId) {
        return Review.builder()
                .id(UUID.randomUUID())
                .userId(USER_ID)
                .raterId(raterId)
                .rating(5)
                .text("Great event!")
                .build();
    }

}
