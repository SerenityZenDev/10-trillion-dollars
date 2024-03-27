package org.example.tentrilliondollars.product.repository;

import java.util.List;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByUser(User user);
}
