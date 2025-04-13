package com.whatstheplan.reviews.client.user;

import com.whatstheplan.reviews.client.user.response.BasicUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class UserClient {

    private final WebClient webClient;

    @Cacheable(value = "userBasicInfoCache", key = "#userId", cacheManager = "userCacheManager")
    public BasicUserResponse getUserBasicInfo(UUID userId) {
        log.info("Retrieving username for userId: {}", userId);
        return webClient.get()
                .uri("/users-info/{userId}", userId)
                .retrieve()
                .bodyToMono(BasicUserResponse.class)
                .doOnNext(response -> log.info("Successfully retrieved username for user id {}: {}", userId, response.getUsername()))
                .block();
    }
}
