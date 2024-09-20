package com.company.ordersservice.configuration;

import com.company.ordersservice.application.OrderService;
import com.company.ordersservice.domain.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.company.ordersservice.infrastructure.jpa")
@EnableTransactionManagement
public class ApplicationConfiguration {

    @Bean
    OrderService orderService(OrderRepository orderRepository) {
        return new OrderService(orderRepository);
    }
}
