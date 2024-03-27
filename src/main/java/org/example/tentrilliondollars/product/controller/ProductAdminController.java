package org.example.tentrilliondollars.product.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.product.dto.request.ProductRequest;
import org.example.tentrilliondollars.product.dto.request.ProductUpdateRequest;
import org.example.tentrilliondollars.product.dto.response.ProductResponse;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.service.ProductService;
import org.example.tentrilliondollars.user.entity.User;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class ProductAdminController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createAdminProduct(
        @RequestBody ProductRequest productRequest
        // @AuthenticationPrincipal Principal principal
    ) {
        // 임시 유저 ID 1L
        User user = new User();
        user.setId(1L);
        productService.createAdminProduct(productRequest, user);

        return ResponseEntity.status(201)
            .body("Product created successfully");
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAdminProducts(
        // @AuthenticationPrincipal Principal principal
    ){
        User user = new User();
        user.setId(1L);

        List<Product> products = productService.getAdminProducts(user);
        List<ProductResponse> productResponses = new ArrayList<>();
        products.forEach(product -> productResponses.add(new ProductResponse(product)));


        return ResponseEntity.status(200)
            .body(productResponses);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateAdminProduct(
        @PathVariable Long productId,
        @RequestBody ProductUpdateRequest productRequest
        // @AuthenticationPrincipal Principal principal
    ) throws NotFoundException {
        User user = new User();
        user.setId(1L);

        productService.updateAdminProduct(productId, productRequest, user);

        return ResponseEntity.status(200)
            .body("Product update successfully");
    }

    @PatchMapping
    public void updateAdminProductStock(
        // @AuthenticationPrincipal Principal principal
    ){

    }

    @DeleteMapping
    public void deleteAdminProduct(
        // @AuthenticationPrincipal Principal principal
    ){

    }
}
