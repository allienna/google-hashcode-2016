package fr.allienna.google.hashcode.model;

/**
 * Created by aurelienallienne on 12/02/2016.
 */
public class ProductGroup {

    private Product product;
    private int quantity;

    public ProductGroup(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductGroup{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
