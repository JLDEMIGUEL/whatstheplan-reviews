package com.whatstheplan.reviews.testconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatstheplan.reviews.repository.ReviewRepository;
import com.whatstheplan.reviews.testconfig.wiremock.AuthWireMockExtension;
import com.whatstheplan.reviews.testconfig.wiremock.UserWireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.whatstheplan.reviews.testconfig.utils.DataUtils.OTHER_RATER_ID;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.OTHER_RATER_USERNAME;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.RATER_ID;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.RATER_USERNAME;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.USERNAME;
import static com.whatstheplan.reviews.testconfig.utils.DataUtils.USER_ID;
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

    @RegisterExtension
    protected static UserWireMockExtension userWireMockExtension = new UserWireMockExtension();
    @RegisterExtension
    protected static AuthWireMockExtension authWireMockExtension = new AuthWireMockExtension();

    protected static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor JWT_TOKEN = jwt()
            .jwt(jwt -> jwt.claim("sub", RATER_ID))
            .authorities(new SimpleGrantedAuthority("ROLE_user"));

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        authWireMockExtension.stubForToken();
        userWireMockExtension.stubForUser(USER_ID, USERNAME);
        userWireMockExtension.stubForUser(RATER_ID, RATER_USERNAME);
        userWireMockExtension.stubForUser(OTHER_RATER_ID, OTHER_RATER_USERNAME);
    }

}
