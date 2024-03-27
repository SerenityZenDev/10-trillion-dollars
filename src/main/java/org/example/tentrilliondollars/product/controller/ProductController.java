package org.example.tentrilliondollars.product.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.product.dto.response.ProductDetailResponse;
import org.example.tentrilliondollars.product.dto.response.ProductResponse;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.service.ProductService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    /**
     * todo : 페이징 처리 추가
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponse> productResponses = new ArrayList<>();
        products.forEach(product -> productResponses.add(new ProductResponse(product)));

        return ResponseEntity.status(200)
            .body(productResponses);
    }

    /**
     * todo : "쿼리가 2개 나가는 현상 수정"
     */

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(
        @PathVariable Long productId
    ) throws NotFoundException {
        return ResponseEntity.status(200)
            .body(new ProductDetailResponse(productService.getProductDetail(productId)));
    }


}
