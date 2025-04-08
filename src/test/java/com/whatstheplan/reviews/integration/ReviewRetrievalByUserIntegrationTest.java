package com.whatstheplan.reviews.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.model.response.ErrorResponse;
import com.whatstheplan.reviews.model.response.ReviewResponse;
import com.whatstheplan.reviews.repository.ReviewRepository;
import com.whatstheplan.reviews.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static com.whatstheplan.reviews.testconfig.utils.AssertionsUtils.assertReviewResponse;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.OTHER_RATER_ID;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.OTHER_RATER_USERNAME;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.RATER_ID;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.RATER_USERNAME;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.USER_ID;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.generateReviewEntity;
import static java.util.Collections.reverseOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewRetrievalByUserIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
    }

    @Test
    void whenRetrievingReviewsByUserId_thenShouldReturnReviews() throws Exception {
        // given
        Review entity1 = reviewRepository.save(generateReviewEntity(RATER_ID));
        Thread.sleep(10);
        Review entity2 = reviewRepository.save(generateReviewEntity(RATER_ID));
        Thread.sleep(10);
        Review entity3 = reviewRepository.save(generateReviewEntity(OTHER_RATER_ID));

        // when
        MvcResult result = mockMvc.perform(get("/reviews/user/{userId}", USER_ID)
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // then
        List<ReviewResponse> responses = objectMapper.readValue(result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, ReviewResponse.class));

        assertThat(responses)
                .extracting(ReviewResponse::getCreatedAt)
                .isSortedAccordingTo(reverseOrder());

        assertThat(responses).hasSize(3);
        assertReviewResponse(entity3, responses.get(0), false, OTHER_RATER_USERNAME);
        assertReviewResponse(entity2, responses.get(1), true, RATER_USERNAME);
        assertReviewResponse(entity1, responses.get(2), true, RATER_USERNAME);
    }

    @Test
    void whenRetrievingReviewsByNonExistingUserId_thenShouldReturnEmptyList() throws Exception {
        // given
        UUID nonExistingUserId = UUID.randomUUID();

        // when
        MvcResult result = mockMvc.perform(get("/reviews/user/{userId}", nonExistingUserId)
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // then
        List<ReviewResponse> responses = objectMapper.readValue(result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, ReviewResponse.class));

        assertThat(responses).isEmpty();
    }

    @Test
    void whenRetrievingReviewsWithInvalidUserIdFormat_thenShouldReturnBadRequest() throws Exception {
        // given
        String invalidUserId = "invalid-uuid";

        // when
        MvcResult result = mockMvc.perform(get("/reviews/user/{userId}", invalidUserId)
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        ErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(response.getReason()).contains("Invalid UUID string: " + invalidUserId);
    }

    @Test
    void whenRetrievingReviewsWithoutAuthToken_thenShouldReturnUnauthorized() throws Exception {
        // given
        UUID userId = UUID.randomUUID();

        // when
        mockMvc.perform(get("/reviews/user/{userId}", userId)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());

        // then
        assertThat(reviewRepository.count()).isEqualTo(0);
    }

}
