package org.example.tentrilliondollars.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.tentrilliondollars.global.TimeStamped;
import org.example.tentrilliondollars.product.dto.request.ProductRequest;
import org.example.tentrilliondollars.product.dto.request.ProductUpdateRequest;
import org.example.tentrilliondollars.product.dto.request.StockUpdateRequest;
import org.example.tentrilliondollars.user.entity.User;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private Long price;
    @Column
    private String description;
    @Column
    private Long stock;
    @Column
    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Builder
    public Product(String name, Long price, String description, Long stock, String photo, User user) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.photo = photo;
        this.user = user;
    }

    public void update(ProductUpdateRequest productRequest) {
        this.name = productRequest.getName();
        this.price = productRequest.getPrice();
        this.description = productRequest.getDescription();
        this.photo = productRequest.getPhoto();
    }

    public void updateStock(StockUpdateRequest stockupdateRequest) {
        this.stock = stockupdateRequest.getStock();
    }
}
