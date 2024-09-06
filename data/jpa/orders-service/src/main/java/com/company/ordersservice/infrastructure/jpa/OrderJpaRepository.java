package com.company.ordersservice.infrastructure.jpa;

import com.company.ordersservice.domain.Order;
import com.company.ordersservice.domain.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderJpaRepository extends OrderRepository, JpaRepository<Order,Long> {

    @Override
    default Order store(Order order){
        return save(order);
    }

    @Override
    default Optional<Order> find(Long id){
        return findById(id);
    }
}
