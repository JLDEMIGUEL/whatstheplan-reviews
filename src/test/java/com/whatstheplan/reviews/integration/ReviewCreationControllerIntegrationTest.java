package com.whatstheplan.reviews.integration;

import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.model.request.ReviewRequest;
import com.whatstheplan.reviews.model.response.ErrorResponse;
import com.whatstheplan.reviews.model.response.ReviewResponse;
import com.whatstheplan.reviews.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static com.whatstheplan.reviews.testconfig.utils.AssertionsUtils.assertReviewEntity;
import static com.whatstheplan.reviews.testconfig.utils.AssertionsUtils.assertReviewResponse;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.generateReviewRequest;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

class ReviewCreationControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void whenCreatingANewReview_thenShouldStoreReviewSuccessfully() throws Exception {
        // given
        ReviewRequest reviewRequest = generateReviewRequest();

        // when
        MvcResult result = mockMvc.perform(post("/reviews")
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(reviewRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        Review savedReview = reviewRepository.findAll().get(0);
        ReviewResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ReviewResponse.class);

        assertReviewResponse(reviewRequest, response, true);
        assertReviewEntity(reviewRequest, savedReview);
    }


    @Test
    void whenCreatingReviewWithInvalidRating_thenShouldReturnBadRequest() throws Exception {
        // given
        ReviewRequest reviewRequest = generateReviewRequest();
        reviewRequest.setRating(6);

        // when
        MvcResult result = mockMvc.perform(post("/reviews")
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(reviewRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        ErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(response.getReason()).contains("Rating must not exceed 5");
        assertThat(reviewRepository.count()).isEqualTo(0);
    }

    @Test
    void whenCreatingReviewWithMissingText_thenShouldReturnBadRequest() throws Exception {
        // given
        ReviewRequest reviewRequest = generateReviewRequest();
        reviewRequest.setText(EMPTY);
        // when
        MvcResult result = mockMvc.perform(post("/reviews")
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(reviewRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        ErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(response.getReason()).contains("Review text cannot be blank");
        assertThat(reviewRepository.count()).isEqualTo(0);
    }

    @Test
    void whenCreatingReviewWithoutAuthToken_thenShouldReturnUnauthorized() throws Exception {
        // given
        ReviewRequest reviewRequest = generateReviewRequest();

        // when
        mockMvc.perform(post("/reviews")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(reviewRequest)))
                .andExpect(status().isUnauthorized());

        // then
        assertThat(reviewRepository.count()).isEqualTo(0);
    }
}
