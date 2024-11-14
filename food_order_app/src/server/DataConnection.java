package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface DataConnection {

    static final String URL = "jdbc:mysql://localhost:3306/food_ordering";
    static final String USER = "root";
    static final String PASSWORD = "root";  // Path to your SQLite database file

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            return null;  // Return null if connection fails
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Failed to close connection: " + e.getMessage());
            }
        }
    }
}
