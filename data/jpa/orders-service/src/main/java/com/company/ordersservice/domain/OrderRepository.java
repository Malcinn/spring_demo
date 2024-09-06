package com.company.ordersservice.domain;

import java.util.Optional;

public interface OrderRepository {
    Order store(Order order);
    Optional<Order> find(Long id);
}
