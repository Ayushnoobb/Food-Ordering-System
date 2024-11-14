package server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> items; // Food name -> Quantity

    public Cart() {
        items = new HashMap<>();
    }

    public void addItem(String name, int quantity) {
        items.put(name, items.getOrDefault(name, 0) + quantity);
    }

    public void removeItem(String name) {
        items.remove(name);
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    @Override
    public String toString() {
        StringBuilder cartDetails = new StringBuilder("Cart:\n");
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            cartDetails.append(entry.getKey()).append(" x ").append(entry.getValue()).append("\n");
        }
        return cartDetails.toString();
    }
}

