package org.example.tentrilliondollars.review.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.example.tentrilliondollars.review.dto.ReviewRequest;
import org.example.tentrilliondollars.review.dto.ReviewResponse;
import org.example.tentrilliondollars.review.entity.Review;
import org.example.tentrilliondollars.review.repository.ReviewRepository;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    //리뷰 생성
    public void createReview(Long productId, ReviewRequest reviewRequest, Long userId) {
        Product product = findProductByIdOrThrow(productId);
        User user = findUserByIdOrThrow(userId);
        Review review = new Review(reviewRequest, product, user);
        reviewRepository.save(review);
    }
    // 게시글 전체 조회
    public List<ReviewResponse> getAllReviews() {
        List<Review> reviewList = reviewRepository.findAll();
        return reviewList.stream()
            .map(ReviewResponse::new)
            .collect(Collectors.toList());
    }
    //리뷰 선택 조회
    public ReviewResponse getReview(Long reviewId) {
        Review review = findReviewByIdOrThrow(reviewId);
        return new ReviewResponse(review);
    }
    //리뷰 삭제
    public void deleteReview(Long reviewId) {
        Review review = findReviewByIdOrThrow(reviewId);
        reviewRepository.delete(review);
    }
    @Transactional
    public void updateReview(Long reviewId,ReviewRequest reviewRequest) {
        Review review = findReviewByIdOrThrow(reviewId);
        review.UpdateReview(reviewRequest);
    }
    //=====================================================
    public Review findReviewByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
    }
    //상품 검증 메서드
    public Product findProductByIdOrThrow(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }
    //유저 검증 메서드
    public User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

}
