package fr.allienna.google.hashcode.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by aurelienallienne on 12/02/2016.
 */
public class Order {

    private final int id;
    private List<ProductGroup> items;
    private Position destination;
    private boolean fulfilld;

    public Order(int id, List<ProductGroup> items, Position destination, boolean fulfilld) {
        this.id = id;
        this.items = items;
        this.destination = destination;
        this.fulfilld = fulfilld;
    }

    public int getId() {
        return id;
    }

    public List<ProductGroup> getItems() {
        return items;
    }

    public void setItems(List<ProductGroup> items) {
        this.items = items;
    }

    public Position getDestination() {
        return destination;
    }

    public void setDestination(Position destination) {
        this.destination = destination;
    }

    public boolean isFulfilld() {
        return fulfilld;
    }

    public void setFulfilld(boolean fulfilld) {
        this.fulfilld = fulfilld;
    }

    public void receive(ProductGroup product) {
        ProductGroup deliveredProduct = items.stream()
                .filter(productGroup -> productGroup.getProduct().equals(product.getProduct()))
                .findFirst().get();

        deliveredProduct.setQuantity(deliveredProduct.getQuantity() - product.getQuantity());

        setFulfilld(!items.stream().anyMatch(item -> item.getQuantity() > 0));
    }

    @Override
    public String toString() {
        return "Order{" +
                "items=" + items +
                ", destination=" + destination +
                ", fulfilld=" + fulfilld +
                '}';
    }
}
