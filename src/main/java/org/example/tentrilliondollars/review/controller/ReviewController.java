package org.example.tentrilliondollars.review.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.review.dto.ReviewRequest;
import org.example.tentrilliondollars.review.dto.ReviewResponse;
import org.example.tentrilliondollars.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/{productId}")
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping("/reviews")
    public ResponseEntity<String> createReview(
        @PathVariable Long productId,
        @RequestBody ReviewRequest reviewRequest
    ) {
        long userId = 1;
        reviewService.createReview(productId, reviewRequest, userId);
        return ResponseEntity.ok().body("후기가 등록 되었습니다.");
    }
    @GetMapping("/reviews")
    //리뷰 전체 조회
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> responseList = reviewService.getAllReviews();
        return ResponseEntity.ok().body(responseList);
    }
    //리뷰 선택 조회
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId) {
        ReviewResponse reviewResponse = reviewService.getReview(reviewId);
        return ResponseEntity.ok().body(reviewResponse);
    }
    //리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().body("리뷰가 삭제 되었습니다.");
    }
    //리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId,@RequestBody ReviewRequest reviewRequest) {
        reviewService.updateReview(reviewId,reviewRequest);
        return ResponseEntity.ok().body("리뷰가 수정 되었습니다.");
    }
}
