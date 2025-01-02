package com.whatstheplan.reviews.integration;

import com.whatstheplan.reviews.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewsControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testEndpoint_ShouldReturnUp() throws Exception {
        mockMvc.perform(get("/reviews/test"))
                .andExpect(status().isOk());
    }
}