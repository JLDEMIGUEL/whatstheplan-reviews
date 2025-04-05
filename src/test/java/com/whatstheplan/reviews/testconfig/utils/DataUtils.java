package com.whatstheplan.reviews.testconfig.utils;

import com.whatstheplan.reviews.client.user.response.BasicUserResponse;
import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.model.request.ReviewRequest;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class DataUtils {

    public final static UUID RATER_ID = UUID.randomUUID();
    public final static UUID USER_ID = UUID.randomUUID();
    public final static UUID OTHER_RATER_ID = UUID.randomUUID();

    public static final String USERNAME = "username";
    public static final String RATER_USERNAME = "rater-username";
    public static final String OTHER_RATER_USERNAME = "other-rater-username";


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

    public static BasicUserResponse generateBasicUserResponse(String username) {
        return BasicUserResponse.builder()
                .username(username)
                .build();
    }
}
