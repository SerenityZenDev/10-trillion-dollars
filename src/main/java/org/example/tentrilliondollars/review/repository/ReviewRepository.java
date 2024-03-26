package org.example.tentrilliondollars.review.repository;

import org.example.tentrilliondollars.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
