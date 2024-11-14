package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodMenuImpl extends UnicastRemoteObject implements FoodMenu {
    private static final long serialVersionUID = 1L;
    private final List<Food> menu;
    private final Cart cart;

    protected FoodMenuImpl() throws RemoteException {
        super();
        menu = new ArrayList<>();
        cart = new Cart();

        // Initialize menu items
        menu.add(new Food("Burger", 5.99, "A delicious beef burger"));
        menu.add(new Food("Pizza", 8.99, "Cheese pizza with toppings"));
        menu.add(new Food("Pasta", 7.99, "Creamy Alfredo pasta"));
        menu.add(new Food("Salad", 4.99, "Fresh garden salad"));
    }
    
    public List<Food> getMenu() throws RemoteException {
        return menu;
    }

    @Override
    public Cart getCart() throws RemoteException {
        return cart;
    }

    @Override
    public void addToCart(String foodName, int quantity) throws RemoteException {
        Food food = getFoodItem(foodName);
        if (food != null) {
            cart.addItem(foodName, quantity);
        }
    }

    @Override
    public void removeFromCart(String foodName) throws RemoteException {
        cart.removeItem(foodName);
    }
    
    @Override
    public String placeOrder(Map<String, Integer> order) throws RemoteException {
        double total = 0.0;
        StringBuilder receipt = new StringBuilder("Order Receipt:\n");
        for (Map.Entry<String, Integer> entry : order.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            Food food = getFoodItem(itemName);
            if (food != null) {
                double price = food.getPrice() * quantity;
                total += price;
                receipt.append(food.getName()).append(" x ").append(quantity).append(" = $").append(price).append("\n");
            } else {
                receipt.append("Item '").append(itemName).append("' not found.\n");
            }
        }
        receipt.append("Total: $").append(total);
        return receipt.toString();
    }

    @Override
    public Order checkout() throws RemoteException {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : cart.getItems().entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            Food food = getFoodItem(itemName);
            if (food != null) {
                total += food.getPrice() * quantity;
            }
        }
        Order order = new Order(cart.getItems(), total);
        cart.clear(); // Clear the cart after checkout
        return order;
    }

    public Food getFoodItem(String name) {
        return menu.stream().filter(f -> f.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}


