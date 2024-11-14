package server;

import java.io.Serializable;
import java.util.Map;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Integer> items;
    private  Map<String, Double> itemPrices;
    private final double totalAmount;

    public Order(Map<String, Integer> items, Map<String, Double> itemPrices , double totalAmount) {
        this.items = items;
        this.totalAmount = totalAmount;
    }
    
    public double getItemPrice(String itemName) {
        return itemPrices.getOrDefault(itemName, 0.0);
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        StringBuilder orderDetails = new StringBuilder("Order Summary:\n");
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            orderDetails.append(entry.getKey()).append(" x ").append(entry.getValue()).append("\n");
        }
        orderDetails.append("Total Amount: $").append(totalAmount).append("\n");
        return orderDetails.toString();
    }
}

