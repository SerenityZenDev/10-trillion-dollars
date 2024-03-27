package org.example.tentrilliondollars.product.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.product.dto.request.ProductRequest;
import org.example.tentrilliondollars.product.dto.request.ProductUpdateRequest;
import org.example.tentrilliondollars.product.dto.request.StockUpdateRequest;
import org.example.tentrilliondollars.product.dto.response.ProductResponse;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.service.ProductService;
import org.example.tentrilliondollars.user.entity.User;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
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
        user.setRole("admin");
        productService.createAdminProduct(productRequest, user);

        return ResponseEntity.status(201)
            .body("Product created successfully");
    }

    @GetMapping
    public List<ProductResponse> getAdminProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
        // @AuthenticationPrincipal Principal principal
    ){
        User user = new User();
        user.setId(1L);
        user.setRole("admin");

        Pageable pageable = PageRequest.of(page, size);

        return productService.getAdminProducts(user, pageable);

    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateAdminProduct(
        @PathVariable Long productId,
        @RequestBody ProductUpdateRequest productRequest
        // @AuthenticationPrincipal Principal principal
    ) throws NotFoundException {
        User user = new User();
        user.setId(1L);
        user.setRole("admin");

        productService.updateAdminProduct(productId, productRequest, user);

        return ResponseEntity.status(200)
            .body("Product update successfully");
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<String> updateAdminProductStock(
        @PathVariable Long productId,
        @RequestBody StockUpdateRequest stockupdateRequest
        // @AuthenticationPrincipal Principal principal
    ) throws NotFoundException {
        User user = new User();
        user.setId(1L);
        user.setRole("admin");

        productService.updateAdminProductStock(productId, stockupdateRequest, user);

        return ResponseEntity.status(200)
            .body("Product stock update successfully");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteAdminProduct(
        @PathVariable Long productId
        // @AuthenticationPrincipal Principal principal
    ) throws NotFoundException {
        User user = new User();
        user.setId(1L);
        user.setRole("admin");

        productService.deleteAdminProduct(productId, user);

        return ResponseEntity.status(200)
            .body("Product delete successfully");
    }
}
