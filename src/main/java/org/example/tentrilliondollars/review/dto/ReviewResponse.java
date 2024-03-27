package org.example.tentrilliondollars.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.review.entity.Review;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String content;
    private String photo;
    private Long score;
    public ReviewResponse(Review review){
        this.content = review.getContent();
        this.photo = review.getPhoto();
        this.score = review.getScore();
    }
}
