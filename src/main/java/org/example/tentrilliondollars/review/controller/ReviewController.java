package org.example.tentrilliondollars.review.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.global.security.UserDetailsImpl;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.review.dto.ReviewRequest;
import org.example.tentrilliondollars.review.dto.ReviewResponse;
import org.example.tentrilliondollars.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
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
        @RequestBody ReviewRequest reviewRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        reviewService.createReview(productId, reviewRequest, userDetails.getUser().getId());
        return ResponseEntity.ok().body("후기가 등록 되었습니다.");
    }
    @GetMapping("/reviews")
    //리뷰 전체 조회
    public ResponseEntity<List<ReviewResponse>> getAllReviews(
        @PathVariable Long productId
    ) {
        List<ReviewResponse> responseList = reviewService.getAllReviews();
        return ResponseEntity.ok().body(responseList);
    }
    //리뷰 선택 조회
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(
        @PathVariable Long productId,
        @PathVariable Long reviewId
    ) {
        ReviewResponse reviewResponse = reviewService.getReview(reviewId,productId);
        return ResponseEntity.ok().body(reviewResponse);
    }
    //리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(
        @PathVariable Long reviewId,
        @PathVariable Long productId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        reviewService.deleteReview(reviewId,userDetails.getUser().getId(),productId);
        return ResponseEntity.ok().body("리뷰가 삭제 되었습니다.");
    }
    //리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(
        @PathVariable Long reviewId,
        @RequestBody ReviewRequest reviewRequest,
        @PathVariable Long productId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        reviewService.updateReview(reviewId,reviewRequest,userDetails.getUser().getId(),productId);
        return ResponseEntity.ok().body("리뷰가 수정 되었습니다.");
    }



}
