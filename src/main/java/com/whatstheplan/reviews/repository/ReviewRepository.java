package com.whatstheplan.reviews.repository;

import com.whatstheplan.reviews.model.entity.Review;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends MongoRepository<Review, UUID> {
    List<Review> findByUserId(UUID userId, Sort sort);

    default List<Review> findByUserId(UUID userId) {
        return findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
