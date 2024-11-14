package server;
import java.io.Serializable;

public class Food implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private double price;
    private String description;

    public Food(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " - $" + price + " (" + description + ")";
    }
}
