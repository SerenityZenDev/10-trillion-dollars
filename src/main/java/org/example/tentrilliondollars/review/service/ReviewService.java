package org.example.tentrilliondollars.review.service;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

}
