package fr.allienna.google.hashcode.model;

/**
 * Created by aurelienallienne on 12/02/2016.
 */
public class Product {

    private int id;
    private int weight;

    public Product(int id, int weight) {
        this.id = id;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", weight=" + weight +
                '}';
    }
}
