package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodMenuImpl extends UnicastRemoteObject implements FoodMenu {
    private static final long serialVersionUID = 1L;
    private final Cart cart;
    private final List<Food> menu;

    protected FoodMenuImpl() throws RemoteException {
        super();
        menu = new ArrayList<>();
        cart = new Cart();
    }
    
    @Override
    public Cart getCart() throws RemoteException {
        return cart;
    }
    
    public Food getFoodItem(String name) {
        return menu.stream().filter(f -> f.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public List<Food> getMenu() throws RemoteException {
        List<Food> menu = new ArrayList<>();
        
        // Use DataConnection to get a connection to the database
        Connection conn = DataConnection.getConnection();
        if (conn != null) {
            String query = "SELECT name, price, description FROM food";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                // Loop through the result set and add each item to the menu list
                while (rs.next()) {
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    String description = rs.getString("description");
                    menu.add(new Food(name, price, description));
                }
            } catch (SQLException e) {
                System.err.println("Error fetching menu: " + e.getMessage());
            } finally {
                DataConnection.closeConnection(conn);
            }
        } else {
            System.err.println("Failed to establish database connection.");
        }
        
        return menu;
    }

    @Override
    public void addToCart(String foodName, int quantity) throws RemoteException {
        cart.addItem(foodName, quantity);
    }

    @Override
    public void removeFromCart(String foodName) throws RemoteException {
        cart.removeItem(foodName);
    }

    @Override
    public Order checkout() throws RemoteException {
        double total = 0.0;
        Map<String, Integer> items = null; // Holds item names and quantities
        Map<String, Double> itemPrices = null; // Holds item names and prices

        Connection conn = DataConnection.getConnection();
        if (conn != null) {
            try {
                for (String itemName : cart.getItems().keySet()) {
                    int quantity = cart.getItems().get(itemName);
                    items.put(itemName, quantity); // Add item to items map with quantity
                    
                    // Retrieve item details from the database
                    String query = "SELECT name, price, description FROM food WHERE name = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, itemName);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                double price = rs.getDouble("price");
                                total += price * quantity;
                                itemPrices.put(itemName, price); // Add item to itemPrices map with price
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error during checkout: " + e.getMessage());
            } finally {
                DataConnection.closeConnection(conn);
            }
        }

        cart.clear(); // Clear the cart after checkout

        // Return a new Order with the correct parameters
        return new Order(items, itemPrices, total);
    }
    @Override
    public String placeOrder(Map<String, Integer> orderItems) throws RemoteException {
        double total = 0.0;
        Map<String, Double> itemPrices = null;

        Connection conn = DataConnection.getConnection();
        if (conn != null) {
            try {
                for (Map.Entry<String, Integer> entry : orderItems.entrySet()) {
                    String foodName = entry.getKey();
                    int quantity = entry.getValue();
                    
                    // Retrieve item details from the database
                    String query = "SELECT name, price, description FROM food WHERE name = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, foodName);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                double price = rs.getDouble("price");
                                total += price * quantity;
                                itemPrices.put(foodName, price);
                            }
                        }
                    }
                }
                return "aysuh";
            } catch (SQLException e) {
                System.err.println("Error processing order: " + e.getMessage());
            } finally {
                DataConnection.closeConnection(conn);
            }
        } else {
            System.err.println("Failed to establish database connection.");
        }

        Order order = new Order(orderItems, itemPrices, total);
        System.out.println("Order placed with total: " + total);
		return null;
    }


}
