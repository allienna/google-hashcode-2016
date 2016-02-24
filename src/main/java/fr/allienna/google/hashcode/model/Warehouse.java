package fr.allienna.google.hashcode.model;

import java.util.List;
import java.util.Optional;

/**
 * Created by aurelienallienne on 12/02/2016.
 */
public class Warehouse {

    private final int id;
    private Position position;
    private List<ProductGroup> stocks;

    public Warehouse(int id, Position position, List<ProductGroup> stocks) {
        this.id = id;
        this.position = position;
        this.stocks = stocks;
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

    public List<ProductGroup> getStocks() {
        return stocks;
    }

    public void setStocks(List<ProductGroup> stocks) {
        this.stocks = stocks;
    }

    public boolean contains(ProductGroup stock) {
        int id = stock.getProduct().getId();
        Optional<ProductGroup> optionalStock = stocks.stream().filter(s -> s.getProduct().getId() == id).findFirst();
        if(optionalStock.isPresent()) {
            ProductGroup currentStock = optionalStock.get();
            return currentStock.getQuantity() >= stock.getQuantity();
        }
        return false;
    }

    public ProductGroup pick(ProductGroup stock) {
        for( ProductGroup product : stocks) {
            if(product.getProduct().equals(stock.getProduct())) {
                int quantity = Math.min(stock.getQuantity(), product.getQuantity());
                product.setQuantity( product.getQuantity() - quantity);
                return new ProductGroup(product.getProduct(), quantity);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "position=" + position +
                ", stocks=" + stocks +
                '}';
    }
}
