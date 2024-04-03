package org.example.tentrilliondollars.product.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.product.dto.request.ProductRequest;
import org.example.tentrilliondollars.product.dto.request.ProductUpdateRequest;
import org.example.tentrilliondollars.product.dto.request.StockUpdateRequest;
import org.example.tentrilliondollars.product.dto.response.ProductDetailResponse;
import org.example.tentrilliondollars.product.dto.response.ProductResponse;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.entity.UserRoleEnum;
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
        Page<Product> productPage = productRepository.findAllByStateTrue(pageable);
        return getPageResponse(productPage);
    }

    public ProductDetailResponse getProductDetail(Long productId) throws NotFoundException {
        Product product = getProduct(productId);
        checkProductStateIsFalse(product);
        return new ProductDetailResponse(product);
    }

    public List<ProductResponse> getAllProductsBySearch(String search, Pageable pageable) {
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCaseAndStateTrue(search, pageable);
        return getPageResponse(productPage);
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
        Page<Product> productPage = productRepository.findAllByUserAndStateTrue(user, pageable);
        return getPageResponse(productPage);
    }

    @Transactional
    public void updateAdminProduct(Long productId, ProductUpdateRequest productRequest, User user)
        throws NotFoundException {
        Product product = getProduct(productId);

        checkProductStateIsFalse(product);

        validateProductOwner(user, product);

        product.update(productRequest);
    }

    @Transactional
    public void updateAdminProductStock(Long productId, StockUpdateRequest stockupdateRequest,
        User user)
        throws NotFoundException {
        Product product = getProduct(productId);

        checkProductStateIsFalse(product);

        validateProductOwner(user, product);

        product.updateStock(stockupdateRequest);
    }

    @Transactional
    public void deleteAdminProduct(Long productId, User user) throws NotFoundException {
        Product product = getProduct(productId);

        checkProductStateIsFalse(product);

        validateProductOwner(user, product);

        product.delete();
    }

    private void checkProductStateIsFalse(Product product) {
        if (!product.isState()){
            throw new IllegalArgumentException("해당 상품은 삭제되었습니다.");
        }
    }

    public Product getProduct(Long productId) throws NotFoundException {
        return productRepository.findById(productId).orElseThrow(
            () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
    }

    private void validateUserRole(User user) {
        if (!user.getRole().equals(UserRoleEnum.SELLER)) {
            throw new IllegalArgumentException("인증되지 않은 유저입니다.");
        }
    }

    private void validateProductOwner(User user, Product product) {
        if (!product.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 상품의 권한유저가 아닙니다.");
        }
    }

    private List<ProductResponse> getPageResponse(Page<Product> productPage) {
        return productPage.getContent().stream()
            .map(ProductResponse::new)
            .collect(Collectors.toList());
    }
//---------------------------------------------------------------------------------

}

