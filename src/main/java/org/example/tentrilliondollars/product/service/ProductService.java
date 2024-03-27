package org.example.tentrilliondollars.product.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.product.dto.request.ProductRequest;
import org.example.tentrilliondollars.product.dto.request.ProductUpdateRequest;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.example.tentrilliondollars.user.entity.User;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void createAdminProduct(ProductRequest productRequest, User user) {
        validateUserRole(user);

        Product product = Product.builder()
            .name(productRequest.getName())
            .price(productRequest.getPrice())
            .description(productRequest.getDescription())
            .stock(productRequest.getStock())
            .photo(productRequest.getPhoto())
            .user(user)
            .build();

        productRepository.save(product);
    }

    private void validateUserRole(User user) {
        if (!user.getRole().equals("admin")) {
            throw new IllegalArgumentException("Not authorized");
        }
    }

    public List<Product> getAdminProducts(User user) {
        return productRepository.findAllByUser(user);
    }

    @Transactional
    public void updateAdminProduct(Long productId, ProductUpdateRequest productRequest, User user)
        throws NotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(
            NotFoundException::new
        );

        product.update(productRequest);
    }
}
