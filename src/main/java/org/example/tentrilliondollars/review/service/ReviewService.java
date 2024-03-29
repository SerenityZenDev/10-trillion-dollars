package org.example.tentrilliondollars.review.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.order.repository.OrderDetailRepository;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.example.tentrilliondollars.review.dto.ReviewRequest;
import org.example.tentrilliondollars.review.dto.ReviewResponse;
import org.example.tentrilliondollars.review.entity.Review;
import org.example.tentrilliondollars.review.repository.ReviewRepository;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    //리뷰 생성
    public void createReview(
        Long productId,
        ReviewRequest reviewRequest,
        Long userId
    ) {
        Product product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("상품 정보가 존재하지 않습니다."));
        if(!product.isState()) {
            throw new IllegalArgumentException("삭제된 상품 입니다");
        }
        User user = findUserByIdOrThrow(userId);
        if (!canUserReviewProduct(userId, productId)) {
            throw new IllegalArgumentException("리뷰를 작성할 수 없습니다. 주문 내역을 확인해주세요.");
        }
        Review review = new Review(reviewRequest, product, user);
        reviewRepository.save(review);
    }
    // 게시글 전체 조회
    public List<ReviewResponse> getAllReviews(
        Long productId
    ) {
        List<Review> reviewList = reviewRepository.findByProduct_IdAndProduct_StateTrue(productId);
        return reviewList.stream()
            .map(ReviewResponse::new)
            .collect(Collectors.toList());
    }
    //리뷰 삭제
    public void deleteReview(
        Long reviewId,
        Long userId,
        Long productId
    ) {
        Product product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("상품 정보가 존재하지 않습니다."));
        if(!product.isState()) {
            throw new IllegalArgumentException("삭제된 상품 입니다");
        }
        Review review = findReviewByIdOrThrow(reviewId);
        checkAuthorization(review,userId);
        reviewRepository.delete(review);
    }
    @Transactional
    public void updateReview(
        Long reviewId,
        ReviewRequest reviewRequest,
        Long userId,
        Long productId
    ) {
        Product product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("상품 정보가 존재하지 않습니다."));
        if(!product.isState()) {
            throw new IllegalArgumentException("삭제된 상품 입니다");
        }
        Review review = findReviewByIdOrThrow(reviewId);
        checkAuthorization(review,userId);
        review.UpdateReview(reviewRequest);
    }

    //=====================예외 처리 메서드================================
    //리뷰 유무 메서드
    public Review findReviewByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
    }
    //유저 유무 메서드
    public User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }
    //유저 권한 확인 메서드
    public void checkAuthorization(Review review,Long userId){
        if(!review.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("다른 유저의 게시글을 수정/삭제 하실수 없습니다.");
        }
    }
    // 사용자가 해당 상품을 구매했는지 확인
    private boolean canUserReviewProduct(Long userId, Long productId) {
        long orderCount = orderDetailRepository.countByUserIdAndProductId(userId, productId);
        long reviewCount = reviewRepository.countByUserIdAndProductId(userId, productId);
        return orderCount > reviewCount;
    }

}
