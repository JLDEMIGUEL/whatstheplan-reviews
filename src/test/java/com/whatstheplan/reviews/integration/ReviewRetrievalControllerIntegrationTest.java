package com.whatstheplan.reviews.integration;

import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.model.response.ErrorResponse;
import com.whatstheplan.reviews.model.response.ReviewResponse;
import com.whatstheplan.reviews.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;
import java.util.stream.Stream;

import static com.whatstheplan.reviews.testconfig.utils.AssertionsUtils.assertReviewResponse;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.OTHER_RATER_ID;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.OTHER_RATER_USERNAME;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.RATER_ID;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.RATER_USERNAME;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.generateReviewEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

class ReviewRetrievalControllerIntegrationTest extends BaseIntegrationTest {

    @ParameterizedTest
    @MethodSource("provideTestValues")
    void whenRetrievingExistingReviewById_thenShouldReturnReviewSuccessfully(UUID raterId, String username, boolean isOwned) throws Exception {
        // given
        Review review = reviewRepository.save(generateReviewEntity(raterId));

        // when
        MvcResult result = mockMvc.perform(get("/reviews/{id}", review.getId())
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // then
        ReviewResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ReviewResponse.class);
        assertReviewResponse(review, response, isOwned, username);
    }

    @Test
    void whenRetrievingNonExistingReviewById_thenShouldReturnNotFound() throws Exception {
        // given
        UUID nonExistingId = UUID.randomUUID();

        // when
        MvcResult result = mockMvc.perform(get("/reviews/{id}", nonExistingId)
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();

        // then
        ErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(response.getReason()).contains("Review not found");
    }

    @Test
    void whenRetrievingReviewWithInvalidIdFormat_thenShouldReturnBadRequest() throws Exception {
        // given
        String invalidId = "invalid-uuid-format";

        // when
        MvcResult result = mockMvc.perform(get("/reviews/{id}", invalidId)
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        ErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(response.getReason()).contains("Invalid UUID string: " + invalidId);
    }

    @Test
    void whenRetrievingReviewWithoutAuthToken_thenShouldReturnUnauthorized() throws Exception {
        // given
        UUID randomId = UUID.randomUUID();

        // when
        mockMvc.perform(get("/reviews/{id}", randomId)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());

        // then
        assertThat(reviewRepository.count()).isEqualTo(0);
    }

    public static Stream<Arguments> provideTestValues() {
        return Stream.of(
                Arguments.of(RATER_ID, RATER_USERNAME, true),
                Arguments.of(OTHER_RATER_ID, OTHER_RATER_USERNAME, false)
        );
    }
}