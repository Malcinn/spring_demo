package com.company.ordersservice.application;

import com.company.ordersservice.domain.Order;
import com.company.ordersservice.domain.OrderLine;
import com.company.ordersservice.domain.Product;
import com.company.ordersservice.infrastructure.jpa.OrderJpaRepository;
import com.company.ordersservice.infrastructure.jpa.ProductJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.TestcontainersExtension;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

@SpringBootTest
@Testcontainers
@ExtendWith(value = TestcontainersExtension.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductJpaRepository productRepository;

    @Autowired
    private OrderJpaRepository orderRepository;

    @Container
    static final MySQLContainer<?> mySQLContainer;

    static {
        mySQLContainer = (MySQLContainer) new MySQLContainer(DockerImageName.parse("mysql:8.0-debian"))
                .withDatabaseName("spring-data-jpa-order-service-integration-db")
                .withUsername("testUser")
                .withPassword("testPassword");
        mySQLContainer.start();
    }

    @DynamicPropertySource
    static void setDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }


    @Transactional
    @Test
    public void shouldCreateOrderWithOrderLines() {
        Product p1 = productRepository.store(new Product(null, "Drill", 156.56));
        Product p2 = productRepository.store(new Product(null, "Cordless Drill", 78.12));

        Order order = orderService.createOrder();
        orderService.addProducts(order, List.of(p1, p2));
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);

        Assertions.assertNotNull(order);
        Assertions.assertNotNull(order.getId());
        Assertions.assertEquals(2, order.getLines().size());

        OrderLine first = order.getLines().stream()
                .filter(orderLine -> orderLine.getProduct().getId().equals(p1.getId()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(first);
        Assertions.assertEquals(1, first.getQuantity());

        OrderLine second = order.getLines().stream()
                .filter(orderLine -> orderLine.getProduct().getId().equals(p2.getId()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(second);
        Assertions.assertEquals(5, second.getQuantity());

    }

    @Transactional
    @Test
    public void shouldIncreaseOrderLineQuantityWhenAddingExistingProductToTheOrder() {
        Product p1 = productRepository.store(new Product(null, "Drill", 156.56));
        Product p2 = productRepository.store(new Product(null, "Cordless Drill", 78.12));

        Order order = orderService.createOrder();
        orderService.addProducts(order, List.of(p1, p2));
        orderService.addProduct(order, p1);
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);

        Assertions.assertNotNull(order);
        Assertions.assertNotNull(order.getId());
        Assertions.assertEquals(2, order.getLines().size());

        OrderLine first = order.getLines().stream()
                .filter(orderLine -> orderLine.getProduct().getId().equals(p1.getId()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(first);
        Assertions.assertEquals(2, first.getQuantity());

        OrderLine second = order.getLines().stream()
                .filter(orderLine -> orderLine.getProduct().getId().equals(p2.getId()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(second);
        Assertions.assertEquals(6, second.getQuantity());
    }

    @Transactional
    @Test
    public void productShouldNotBeRemovedAfterOrderIsRemoved() {
        Product p1 = productRepository.store(new Product(null, "Drill", 156.56));
        Product p2 = productRepository.store(new Product(null, "Cordless Drill", 78.12));

        Order order = orderService.createOrder();
        orderService.addProducts(order, List.of(p1, p2));
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);

        Assertions.assertNotNull(order);
        Assertions.assertNotNull(order.getId());
        Assertions.assertEquals(2, order.getLines().size());

        OrderLine first = order.getLines().stream()
                .filter(orderLine -> orderLine.getProduct().getId().equals(p1.getId()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(first);
        Assertions.assertEquals(1, first.getQuantity());

        OrderLine second = order.getLines().stream()
                .filter(orderLine -> orderLine.getProduct().getId().equals(p2.getId()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(second);
        Assertions.assertEquals(5, second.getQuantity());

        //Removing order
        Long orderId = order.getId();
        orderService.removeOrder(order);

        Assertions.assertTrue(orderRepository.findById(orderId).isEmpty());
        Assertions.assertTrue(productRepository.findById(p1.getId()).isPresent());
        Assertions.assertTrue(productRepository.findById(p2.getId()).isPresent());
    }

    @Transactional
    @Test
    public void productShouldNotBeRemovedAfterOrderLineIsRemoved() {
        Product p1 = productRepository.store(new Product(null, "Drill", 156.56));
        Product p2 = productRepository.store(new Product(null, "Cordless Drill", 78.12));

        Order order = orderService.createOrder();
        orderService.addProducts(order, List.of(p1, p2));
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);
        orderService.addProduct(order, p2);

        Assertions.assertNotNull(order);
        Assertions.assertNotNull(order.getId());
        Assertions.assertEquals(2, order.getLines().size());

        OrderLine first = order.getLines().stream()
                .filter(orderLine -> orderLine.getProduct().getId().equals(p1.getId()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(first);
        Assertions.assertEquals(1, first.getQuantity());

        OrderLine second = order.getLines().stream()
                .filter(orderLine -> orderLine.getProduct().getId().equals(p2.getId()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(second);
        Assertions.assertEquals(5, second.getQuantity());

        orderService.removeOrderLine(order, first.getId());

        Order result = orderRepository.findById(order.getId()).orElse(null);
        List<OrderLine> linesResult = result.getLines();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, linesResult.size());
        Assertions.assertTrue(productRepository.findById(p1.getId()).isPresent());
        Assertions.assertTrue(productRepository.findById(p2.getId()).isPresent());
    }
}
