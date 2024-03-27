package org.example.tentrilliondollars.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.product.dto.request.ProductRequest;
import org.example.tentrilliondollars.product.service.ProductService;
import org.example.tentrilliondollars.user.entity.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public String createAdminProduct(
        @RequestBody ProductRequest productRequest
        // @AuthenticationPrincipal Principal principal
    ) {
        // 임시 유저 ID 1L
        User user = new User();
        productService.createAdminProduct(productRequest, user);
        return "Product created successfully";
    }

    @GetMapping
    public void getAdminProducts(
        // @AuthenticationPrincipal Principal principal
    ){

    }

    @PutMapping
    public void updateAdminProduct(
        // @AuthenticationPrincipal Principal principal
    ){

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
