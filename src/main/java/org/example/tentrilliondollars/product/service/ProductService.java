package org.example.tentrilliondollars.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.product.dto.request.ProductRequest;
import org.example.tentrilliondollars.product.dto.request.ProductUpdateRequest;
import org.example.tentrilliondollars.product.dto.request.StockUpdateRequest;
import org.example.tentrilliondollars.product.dto.response.ProductResponse;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.example.tentrilliondollars.user.entity.User;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.getContent().stream()
            .map(ProductResponse::new)
            .collect(Collectors.toList());
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


    public List<ProductResponse> getAdminProducts(User user, Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByUser(user, pageable);
        return productPage.getContent().stream()
            .map(ProductResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateAdminProduct(Long productId, ProductUpdateRequest productRequest, User user)
        throws NotFoundException {
        Product product = getProduct(productId);

        validateProductOwner(user, product);

        product.update(productRequest);
    }



    @Transactional
    public void updateAdminProductStock(Long productId, StockUpdateRequest stockupdateRequest, User user)
        throws NotFoundException {
        Product product = getProduct(productId);

        validateProductOwner(user, product);

        product.updateStock(stockupdateRequest);
    }
    public void deleteAdminProduct(Long productId, User user) throws NotFoundException {
        Product product = getProduct(productId);

        validateProductOwner(user, product);

        productRepository.delete(product);
    }

    private Product getProduct(Long productId) throws NotFoundException {
        return productRepository.findById(productId).orElseThrow(
            NotFoundException::new
        );
    }

    private void validateUserRole(User user) {
        if (!user.getRole().equals("admin")) {
            throw new IllegalArgumentException("Not authorized");
        }
    }

    private void validateProductOwner(User user, Product product) {
        if (!product.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User id not matching");
        }
    }

}

