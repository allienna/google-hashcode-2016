package fr.allienna.google.hashcode.model;

import fr.allienna.google.hashcode.utils.DistanceCalculator;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by aurelienallienne on 12/02/2016.
 */
public class Drone {

    private final int id;
    private Position position;
    private int maximumLoad;
    private List<ProductGroup> products;

    public Drone(int id, Position position, int maximumLoad, List<ProductGroup> products) {
        this.id = id;
        this.position = position;
        this.maximumLoad = maximumLoad;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getMaximumLoad() {
        return maximumLoad;
    }

    public void setMaximumLoad(int maximumLoad) {
        this.maximumLoad = maximumLoad;
    }

    public List<ProductGroup> getProducts() {
        return products;
    }

    public void setProducts(List<ProductGroup> products) {
        this.products = products;
    }


    public boolean canPickUpMoreItems() {
        int load = 0;
        if(products != null) {
            List<Product> p = products.stream().map(item -> item.getProduct())
                    .collect(Collectors.toList());
            for (Product product : p) {
                load += product.getWeight();
            }
            return load < maximumLoad;
        }
        return true;
    }

    public boolean hasProduct() {
        return products != null && !products.isEmpty();
    }

    public boolean isMaxLoaded() {
        return products.stream().mapToInt(p -> p.getProduct().getWeight()).sum() >= maximumLoad;
    }

    public void load(ProductGroup stock) {
        if(products == null) {
            products = new ArrayList<>();
        }
        products.add(stock);
    }

    public int moveTo(Position position) {
        setPosition(position);
        return DistanceCalculator.euclideanDistance(getPosition(), position);
    }

    public ProductGroup deliver(ProductGroup product) {
        ProductGroup loadedProduct = products.stream()
                .filter(productGroup -> productGroup.getProduct().equals(product.getProduct()))
                .findFirst().get();

        products.remove(loadedProduct);
        return loadedProduct;
    }

    @Override
    public String toString() {
        return "Drone{" +
                "position=" + position +
                ", maximumLoad=" + maximumLoad +
                ", products=" + products +
                '}';
    }
}
