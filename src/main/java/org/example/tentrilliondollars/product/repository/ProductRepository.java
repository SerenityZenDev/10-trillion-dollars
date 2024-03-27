package org.example.tentrilliondollars.product.repository;

import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByUser(User user, Pageable pageable);
}
