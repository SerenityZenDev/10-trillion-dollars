package org.example.tentrilliondollars.product.repository;

import org.example.tentrilliondollars.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
