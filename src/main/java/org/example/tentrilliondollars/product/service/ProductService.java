package org.example.tentrilliondollars.product.service;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

}
