package com.company.ordersservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * No usage of mappedBy property OneToMany relationship because relation is unidirectional.
     *
     * @JoinColumn will add additional order_id colum on OrderLine side
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderLine> lines;

    public void addProduct(Product product) {
        lines.stream()
                .filter(orderLine -> orderLine.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(OrderLine::increaseQuantity, () -> {
                    lines.add(new OrderLine(null, product, 1));
                });
    }

    public void addProducts(Collection<Product> products) {
        for (Product product : products){
            addProduct(product);
        }
    }

    public Double calculatePrice() {
        return lines.stream().map(OrderLine::getProduct).map(Product::getPrice).reduce(Double::sum).orElse(0.0);
    }

    public void removeOrderLine(Long lineId) {
        lines.removeIf(orderLine -> orderLine.getId().equals(lineId));
    }

}
