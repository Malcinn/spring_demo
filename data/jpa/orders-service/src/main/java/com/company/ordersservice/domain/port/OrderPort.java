package com.company.ordersservice.domain.port;

import com.company.ordersservice.domain.Order;
import com.company.ordersservice.domain.Product;

import java.util.Collection;

public interface OrderPort {

    Order createOrder();

    void removeOrder(Order order);

    void addProduct(Order order, Product product);

    void addProducts(Order order, Collection<Product> products);

    void removeOrderLine(Order order, Long lineId);
}
