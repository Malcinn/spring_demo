package com.company.ordersservice.application;

import com.company.ordersservice.domain.Order;
import com.company.ordersservice.domain.OrderRepository;
import com.company.ordersservice.domain.Product;
import com.company.ordersservice.domain.port.OrderPort;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * OrderService by implementing "Port" becomes an "Adapter" in hexagonal architecture design
 */
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
public class OrderService implements OrderPort {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder() {
        return orderRepository.store(new Order(null, new ArrayList<>()));
    }

    @Override
    public void removeOrder(Order order) {
        orderRepository.remove(order);
    }

    @Override
    public void  addProduct(Order order, Product product) {
        order.addProduct(product);
        orderRepository.store(order);
    }

    @Override
    public void addProducts(Order order, Collection<Product> product) {
        order.addProducts(product);
        orderRepository.store(order);
    }

    @Override
    public void removeOrderLine(Order order, Long lineId) {
        order.removeOrderLine(lineId);
        orderRepository.store(order);
    }
}
