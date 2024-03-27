package org.example.tentrilliondollars.product.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponse> productResponses = new ArrayList<>();
        products.forEach(product -> productResponses.add(new ProductResponse(product)));

        return ResponseEntity.status(200)
            .body(productResponses);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductDetail(
        @PathVariable Long productId
    ) throws NotFoundException {
        return ResponseEntity.status(200)
            .body(new ProductResponse(productService.getProductDetail(productId)));
    }


}
