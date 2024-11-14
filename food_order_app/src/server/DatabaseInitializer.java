package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface DatabaseInitializer {
    static final String URL = "jdbc:mysql://localhost:3306/food_ordering";
    static final String USER = "root";
    static final String PASSWORD = "root";

    public static void main(String[] args) {
        createTables();
    }

    public static void createTables() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            System.out.println("Connection successful");

            // Create Roles Table
            String rolesTable = "CREATE TABLE IF NOT EXISTS Roles (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name TEXT NOT NULL UNIQUE" +
                    ");";
            stmt.execute(rolesTable);

            // Insert roles if they don't already exist
            insertRoleIfNotExists(conn, "admin");
            insertRoleIfNotExists(conn, "customer");

            // Create Users Table with foreign key reference to Roles table
            String usersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "firstname TEXT NOT NULL," +
                    "lastname TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "passport_number TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE," +
                    "role_id INT NOT NULL," +
                    "FOREIGN KEY(role_id) REFERENCES Roles(id)" +
                    ");";
            stmt.execute(usersTable);

            // Create Food Items Table
            String foodItemsTable = "CREATE TABLE IF NOT EXISTS FoodItems (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name TEXT NOT NULL," +
                    "description TEXT," +
                    "price REAL NOT NULL," +
                    ");";
            stmt.execute(foodItemsTable);

            String ordersTable = "CREATE TABLE IF NOT EXISTS Orders (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "user_id INT NOT NULL," +
                    "order_date TEXT NOT NULL," +
                    "total_amount REAL NOT NULL," +
                    "status TEXT NOT NULL," +
                    "FOREIGN KEY (user_id) REFERENCES Users(id)" +
                    ");";
            stmt.execute(ordersTable);

            // OrderItems Table
            String orderItemsTable = "CREATE TABLE IF NOT EXISTS OrderItems (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "order_id INT NOT NULL," +
                    "item_name TEXT NOT NULL," +
                    "quantity INT NOT NULL," +
                    "price REAL NOT NULL," +
                    "FOREIGN KEY (order_id) REFERENCES Orders(id)" +
                    ");";
            stmt.execute(orderItemsTable);

            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            System.out.println("An error occurred while creating tables: " + e.getMessage());
        }
    }

    // Helper method to check if a role exists, and insert it if not
    private static void insertRoleIfNotExists(Connection conn, String roleName) throws SQLException {
        String checkRoleQuery = "SELECT name FROM Roles WHERE name = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkRoleQuery)) {
            checkStmt.setString(1, roleName);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {  // If the role doesn't exist, insert it
                String insertRoleQuery = "INSERT INTO Roles (name) VALUES (?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertRoleQuery)) {
                    insertStmt.setString(1, roleName);
                    insertStmt.executeUpdate();
                    System.out.println("Role '" + roleName + "' inserted.");
                }
            } else {
                System.out.println("Role '" + roleName + "' already exists.");
            }
        }
    }
}
