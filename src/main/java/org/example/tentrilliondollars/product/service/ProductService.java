package org.example.tentrilliondollars.product.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductDetail(Long productId) throws NotFoundException {
        return productRepository.findById(productId).orElseThrow(
            NotFoundException::new
        );
    }
}
