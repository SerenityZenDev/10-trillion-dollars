package org.example.tentrilliondollars.review.controller;


import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.review.service.ReviewService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

}
