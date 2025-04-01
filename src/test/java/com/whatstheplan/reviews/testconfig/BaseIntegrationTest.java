package com.whatstheplan.reviews.testconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatstheplan.reviews.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.whatstheplan.reviews.testconfig.utils.DataUtils.RATER_ID;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ReviewRepository reviewRepository;
    @Autowired
    protected ObjectMapper objectMapper;

    protected static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor JWT_TOKEN = jwt()
            .jwt(jwt -> jwt.claim("sub", RATER_ID))
            .authorities(new SimpleGrantedAuthority("ROLE_user"));

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
    }

}
