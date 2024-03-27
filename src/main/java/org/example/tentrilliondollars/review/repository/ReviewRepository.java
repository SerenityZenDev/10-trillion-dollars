package org.example.tentrilliondollars.review.repository;

import java.util.List;
import org.example.tentrilliondollars.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    //별점 내림차순 정렬
    List<Review> findAllByOrderByScoreDesc();
}
