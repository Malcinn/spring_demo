package com.company.ordersservice.infrastructure.jpa;

import com.company.ordersservice.domain.Product;
import com.company.ordersservice.domain.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductJpaRepository extends ProductRepository, JpaRepository<Product, Long> {

    @Override
    default Product store(Product product) {
        return save(product);
    }

    @Override
    default Optional<Product> find(Long id) {
        return findById(id);
    }
}
