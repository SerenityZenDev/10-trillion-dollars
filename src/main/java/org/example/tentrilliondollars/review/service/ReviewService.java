package org.example.tentrilliondollars.review.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.order.repository.OrderDetailRepository;
import org.example.tentrilliondollars.order.service.OrderService;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.example.tentrilliondollars.product.service.ProductService;
import org.example.tentrilliondollars.review.dto.ReviewRequest;
import org.example.tentrilliondollars.review.dto.ReviewResponse;
import org.example.tentrilliondollars.review.entity.Review;
import org.example.tentrilliondollars.review.repository.ReviewRepository;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final OrderService orderService;
    //리뷰 생성
    public void createReview(
        Long productId,
        ReviewRequest reviewRequest,
        Long userId
    ) {
        Product product = productService.getProduct(productId);
        productService.checkProductStateIsFalse(product);
        if (!canUserReviewProduct(userId, productId)) {
            throw new IllegalArgumentException("리뷰를 작성할 수 없습니다. 주문 내역을 확인해주세요.");
        }
        if(reviewRequest.getScore()<1||reviewRequest.getScore()>5){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "1점부터 5점까지 입력해주세요");
        }
        Review review = new Review(reviewRequest, productId, userId);
        reviewRepository.save(review);
    }
    // 게시글 전체 조회
    public List<ReviewResponse> getAllReviews(
        Long productId
    ) {
        Product product = productService.getProduct(productId);
        productService.checkProductStateIsFalse(product);

        List<Review> reviewList = reviewRepository.findByProductId(productId);
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
        Product product = productService.getProduct(productId);
        productService.checkProductStateIsFalse(product);
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
        Product product = productService.getProduct(productId);
        productService.checkProductStateIsFalse(product);
        Review review = findReviewByIdOrThrow(reviewId);
        checkAuthorization(review,userId);
        if(reviewRequest.getScore()<1||reviewRequest.getScore()>5){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "1점부터 5점까지 입력해주세요");
        }
        review.updateReview(reviewRequest);
    }

    //=====================예외 처리 메서드================================
    //리뷰 유무 메서드
    public Review findReviewByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."));
    }
    //유저 권한 확인 메서드
    public void checkAuthorization(Review review,Long userId){
        if(!review.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "다른 유저의 게시글을 수정/삭제할 수 없습니다.");
        }
    }
    // 사용자가 해당 상품을 구매했는지 확인
    private boolean canUserReviewProduct(Long userId, Long productId) {
        long orderCount = orderService.countByUserIdAndProductId(userId, productId);
        long reviewCount = reviewRepository.countByUserIdAndProductId(userId, productId);
        return orderCount > reviewCount;
    }

}
