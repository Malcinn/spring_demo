package com.company.ordersservice.domain;

import java.util.Optional;

public interface ProductRepository {
    Product store(Product product);
    Optional<Product> find(Long id);
}
