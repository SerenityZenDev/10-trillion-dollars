package org.example.tentrilliondollars.review.service;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.order.repository.OrderDetailRepository;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.example.tentrilliondollars.review.dto.ReviewRequest;
import org.example.tentrilliondollars.review.dto.ReviewResponse;
import org.example.tentrilliondollars.review.entity.Review;
import org.example.tentrilliondollars.review.repository.ReviewRepository;
import org.example.tentrilliondollars.s3.S3Service;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Value("${review.bucket.name}")
    String bucketName;

    private final S3Service s3Service;
    //리뷰 생성
    public void createReview(
        Long productId,
        ReviewRequest reviewRequest,
        Long userId
    ) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보가 존재하지 않습니다."));
        if (!product.isState()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제된 상품입니다.");
        }
        User user = findUserByIdOrThrow(userId);
        if (!canUserReviewProduct(userId, productId)) {
            throw new IllegalArgumentException("리뷰를 작성할 수 없습니다. 주문 내역을 확인해주세요.");
        }
        if(reviewRequest.getScore()<1||reviewRequest.getScore()>5){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "1점부터 5점까지 입력해주세요");
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
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보가 존재하지 않습니다."));
        if (!product.isState()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제된 상품입니다.");
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
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보가 존재하지 않습니다."));

        if (!product.isState()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제된 상품입니다.");
                }
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
    //유저 유무 메서드
    public User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
    }
    //유저 권한 확인 메서드
    public void checkAuthorization(Review review,Long userId){
        if(!review.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "다른 유저의 게시글을 수정/삭제할 수 없습니다.");
        }
    }
    // 사용자가 해당 상품을 구매했는지 확인
    private boolean canUserReviewProduct(Long userId, Long productId) {
        long orderCount = orderDetailRepository.countByUserIdAndProductId(userId, productId);
        long reviewCount = reviewRepository.countByUserIdAndProductId(userId, productId);
        return orderCount > reviewCount;
    }

    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
    }

    public void uploadReviewImage(Long productId, MultipartFile file) throws IOException {
        String imageKey =UUID.randomUUID().toString();
        s3Service.putObject(
                bucketName,"review-images/%s/%s".formatted(productId,
                        imageKey),
                file.getBytes());
        Review review =getReview(productId);
        review.updateImageId(imageKey);
       reviewRepository.save(review);
    }

    public ResponseEntity<byte[]> getReviewImage(Long productId) throws IOException {
        String imageKey = "review-images/1/"+getReview(productId).getImageKey();
        return s3Service.getProductImage(bucketName,imageKey);
    }


}
