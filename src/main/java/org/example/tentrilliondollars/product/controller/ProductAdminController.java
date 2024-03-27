package org.example.tentrilliondollars.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.product.dto.request.ProductRequest;
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
    @PostMapping
    public void createAdminProduct(
        @RequestBody ProductRequest productRequest
        // @AuthenticationPrincipal Principal principal
    ) {

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
