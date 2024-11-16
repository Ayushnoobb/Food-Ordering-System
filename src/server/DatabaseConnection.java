import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // SQLite URL format for a file-based database
    private static final String URL = "jdbc:sqlite:food_ordering.db";  // Path to your SQLite database file

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
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

    public static void main(String[] args) {
        // Example usage
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Connection established successfully!");
            // Perform database operations here
            closeConnection(conn);  // Close the connection when done
        } else {
            System.err.println("Failed to establish connection.");
        }
    }
}
