package com.whatstheplan.reviews.integration;

import com.whatstheplan.reviews.model.entity.Review;
import com.whatstheplan.reviews.model.response.ErrorResponse;
import com.whatstheplan.reviews.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static com.whatstheplan.reviews.testconfig.utils.DataUtils.RATER_ID;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.generateReviewEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

class ReviewDeletionControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void whenDeletingExistingReviewByOwner_thenShouldDeleteSuccessfully() throws Exception {
        // given
        Review review = reviewRepository.save(generateReviewEntity(RATER_ID));

        // when
        mockMvc.perform(delete("/reviews/{id}", review.getId())
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andReturn();

        // then
        assertThat(reviewRepository.existsById(review.getId())).isFalse();
    }

    @Test
    void whenDeletingReviewNotOwnedByUser_thenShouldReturnForbidden() throws Exception {
        // given
        UUID anotherUserId = UUID.randomUUID();
        Review review = reviewRepository.save(generateReviewEntity(anotherUserId));

        // when
        MvcResult result = mockMvc.perform(delete("/reviews/{id}", review.getId())
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn();

        // then
        ErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(response.getReason()).isEqualTo("You do not have permission to delete this review.");
        assertThat(reviewRepository.existsById(review.getId())).isTrue();
    }

    @Test
    void whenDeletingNonExistingReviewById_thenShouldReturnNotFound() throws Exception {
        // given
        UUID nonExistingId = UUID.randomUUID();

        // when
        MvcResult result = mockMvc.perform(delete("/reviews/{id}", nonExistingId)
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();

        // then
        ErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(response.getReason()).contains("Review not found");
    }

    @Test
    void whenDeletingReviewWithInvalidIdFormat_thenShouldReturnBadRequest() throws Exception {
        // given
        String invalidId = "invalid-uuid-format";

        // when
        MvcResult result = mockMvc.perform(delete("/reviews/{id}", invalidId)
                        .with(JWT_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        ErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(response.getReason()).contains("Invalid UUID string: " + invalidId);
    }

    @Test
    void whenDeletingReviewWithoutAuthToken_thenShouldReturnUnauthorized() throws Exception {
        // given
        UUID randomId = UUID.randomUUID();

        // when
        mockMvc.perform(delete("/reviews/{id}", randomId)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());

        // then
        assertThat(reviewRepository.count()).isEqualTo(0);
    }
}

