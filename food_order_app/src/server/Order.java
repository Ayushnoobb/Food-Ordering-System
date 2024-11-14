package server;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Integer> items;
    private final Map<String, Double> itemPrices;
    private final double totalAmount;

    public Order(Map<String, Integer> items, Map<String, Double> itemPrices, double totalAmount) {
        this.items = items;
        this.itemPrices = itemPrices;
        this.totalAmount = totalAmount;
    }

    // Save the order to the database
    public void saveToDatabase() {
        Connection conn = DataConnection.getConnection();
        if (conn != null) {
            try {
                String insertOrderQuery = "INSERT INTO Orders (total_amount, order_date, status, user_id) VALUES (?, NOW(), 'pending', ?)"; // Sample status and user_id
                try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    orderStmt.setDouble(1, totalAmount);
                    orderStmt.setInt(2, 1); // Use the actual user_id as needed
                    orderStmt.executeUpdate();

                    try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int orderId = generatedKeys.getInt(1);

                            // Insert each item in OrderItems
                            String insertItemQuery = "INSERT INTO OrderItems (order_id, item_name, quantity, price) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement itemStmt = conn.prepareStatement(insertItemQuery)) {
                                for (Map.Entry<String, Integer> entry : items.entrySet()) {
                                    String itemName = entry.getKey();
                                    int quantity = entry.getValue();
                                    double price = itemPrices.getOrDefault(itemName, 0.0);

                                    itemStmt.setInt(1, orderId);
                                    itemStmt.setString(2, itemName);
                                    itemStmt.setInt(3, quantity);
                                    itemStmt.setDouble(4, price);
                                    itemStmt.addBatch();
                                }
                                itemStmt.executeBatch(); // Execute batch insert for items
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error saving order to database: " + e.getMessage());
            } finally {
                DataConnection.closeConnection(conn);
            }
        }
    }

    // Getter methods
    public Map<String, Integer> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getItemPrice(String itemName) {
        return itemPrices.getOrDefault(itemName, 0.0);
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


