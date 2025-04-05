package com.whatstheplan.reviews.testconfig.utils;

import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.model.request.ReviewRequest;
import com.whatstheplan.reviews.model.response.ReviewResponse;
import lombok.experimental.UtilityClass;

import static com.whatstheplan.reviews.testconfig.utils.DataUtils.RATER_ID;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.RATER_USERNAME;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.USERNAME;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

@UtilityClass
public class AssertionsUtils {

    public static void assertReviewEntity(ReviewRequest request, Review savedReview) {
        assertThat(savedReview.getId()).isNotNull();
        assertThat(savedReview.getUserId()).isEqualTo(USER_ID);
        assertThat(savedReview.getRaterId()).isEqualTo(RATER_ID);
        assertThat(savedReview.getRating()).isEqualTo(request.getRating());
        assertThat(savedReview.getText()).isEqualTo(request.getText());
        assertThat(savedReview.getCreatedAt()).isNotNull();
    }

    public static void assertReviewResponse(ReviewRequest request, ReviewResponse response, boolean isOwned) {
        assertThat(response.getId()).isNotNull();
        assertThat(response.getUsername()).isEqualTo(USERNAME);
        assertThat(response.getRaterUsername()).isEqualTo(RATER_USERNAME);
        assertThat(response.getRating()).isEqualTo(request.getRating());
        assertThat(response.getText()).isEqualTo(request.getText());
        assertThat(response.getIsOwnedByUser()).isEqualTo(isOwned);
    }

    public static void assertReviewResponse(Review review, ReviewResponse response, boolean isOwned, String raterUsername) {
        assertThat(response.getId()).isNotNull();
        assertThat(response.getUsername()).isEqualTo(USERNAME);
        assertThat(response.getRaterUsername()).isEqualTo(raterUsername);
        assertThat(response.getRating()).isEqualTo(review.getRating());
        assertThat(response.getText()).isEqualTo(review.getText());
        assertThat(response.getIsOwnedByUser()).isEqualTo(isOwned);
    }
}
