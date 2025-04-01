package com.whatstheplan.reviews.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reviews")
public class Review {

    @Id
    private UUID id;

    private UUID raterId;
    private UUID userId;
    private Integer rating;
    private String text;
    private Instant createdAt = now();
}
