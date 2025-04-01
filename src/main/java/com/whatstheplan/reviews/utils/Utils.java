package com.whatstheplan.reviews.utils;

import com.whatstheplan.reviews.exceptions.InvalidTokenException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class Utils {

    public static UUID getUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .map(UUID::fromString)
                .orElseThrow(() -> new InvalidTokenException("Invalid token, user id not found.", null));
    }

}
