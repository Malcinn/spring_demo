package com.company.ordersservice;

import com.company.ordersservice.domain.*;
import com.company.ordersservice.infrastructure.jpa.OrderJpaRepository;
import com.company.ordersservice.infrastructure.jpa.ProductJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.TestcontainersExtension;
import org.testcontainers.utility.DockerImageName;

import java.util.Iterator;
import java.util.Set;

@Testcontainers
@SpringBootTest
@ExtendWith(TestcontainersExtension.class)
public class OrderServiceTest {

    @Autowired
    private OrderJpaRepository orderRepository;

    @Autowired
    private ProductJpaRepository productRepository;

    @Container
    static final MySQLContainer<?> mySQLContainer;

    static {
        mySQLContainer = (MySQLContainer) new MySQLContainer(DockerImageName.parse("mysql:8.0-debian"))
                .withDatabaseName("spring-data-jpa-order-service-db")
                .withUsername("testUser")
                .withPassword("testPassword");
        //.withInitScript("OrderRepositoryInit.sql");
        mySQLContainer.start();
    }

    @DynamicPropertySource
    static void setDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Test
    public void shouldCreateOrderWithOrderLines() {
        Product p1 = productRepository.store(new Product(null, "Drill", 156.56));
        Product p2 = productRepository.store(new Product(null, "Cordless Drill", 78.12));

        Set<OrderLine> lines = Set.of(new OrderLine(null, p1, 1)
                , new OrderLine(null, p2, 5));

        Order order = new Order(null, lines);

        Assertions.assertNotNull(order);
        Assertions.assertNull(order.getId());
        Assertions.assertEquals(2, order.getLines().size());
        Iterator<OrderLine> orderLineIterator = order.getLines().iterator();
        OrderLine first = orderLineIterator.next();
        OrderLine secondLine = orderLineIterator.next();
        Assertions.assertEquals(1, first.getQuantity());
        Assertions.assertEquals(5, secondLine.getQuantity());
    }

    @Test
    public void shouldIncreaseOrderLineQuantityWhenAddingExistingProductToTheOrder() {

    }

    @Test
    public void productShouldNotBeRemovedAfterOrderIsRemoved() {

    }

    @Test
    public void productShouldNotBeRemovedAfterOrderLineIsRemoved() {

    }
}
