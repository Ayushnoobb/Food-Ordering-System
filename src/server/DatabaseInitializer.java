import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:sqlite:food_ordering.sqlite";

    public static void main(String[] args) {
        createTables();
    }

    public static void createTables() {
        
        try (
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement()) {
            
            System.out.println("out from here");


            // Create Roles Table
            String rolesTable = "CREATE TABLE IF NOT EXISTS Roles (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL UNIQUE" +  // role name (e.g., 'admin', 'customer')
            ");";
            stmt.execute(rolesTable);

            // Insert a role into the Roles table (e.g., 'admin' role)
            String insertRole1 = "INSERT INTO Roles (name) VALUES ('admin');";  // Ensure 'admin' is inserted correctly
            stmt.execute(insertRole1);

            String insertRole2 = "INSERT INTO Roles (name) VALUES ('customer');";  // Ensure 'customer' is inserted correctly
            stmt.execute(insertRole2);

            // Create Users Table with foreign key reference to Roles table
            String usersTable = "CREATE TABLE IF NOT EXISTS Users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "firstname TEXT NOT NULL," +
            "lastname TEXT NOT NULL," +
            "password TEXT NOT NULL," +
            "passport_number TEXT NOT NULL," +
            "email TEXT NOT NULL UNIQUE," +
            "role_id INTEGER NOT NULL," +  // Use role_id as foreign key to reference Roles table
            "FOREIGN KEY(role_id) REFERENCES Roles(id)" +  // Set foreign key constraint
            ");";
            stmt.execute(usersTable);


            // Create Food Items Table
            String foodItemsTable = "CREATE TABLE IF NOT EXISTS FoodItems (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "description TEXT," +
            "price REAL NOT NULL," +
            "category TEXT" +  // e.g., 'main course', 'dessert', etc.
            ");";
            stmt.execute(foodItemsTable);

            // Create Orders Table
            String ordersTable = "CREATE TABLE IF NOT EXISTS Orders (" +
              "id INTEGER PRIMARY KEY AUTOINCREMENT," +
              "user_id INTEGER NOT NULL," +
              "order_date TEXT NOT NULL," +
              "total_price REAL NOT NULL," +
              "status TEXT NOT NULL," +  // e.g., 'pending', 'completed', 'canceled'
              "FOREIGN KEY (user_id) REFERENCES Users(id)" +
              ");";
            stmt.execute(ordersTable);

            // Create Order Details Table
            String orderDetailsTable = "CREATE TABLE IF NOT EXISTS OrderDetails (" +
              "id INTEGER PRIMARY KEY AUTOINCREMENT," +
              "order_id INTEGER NOT NULL," +
              "food_item_id INTEGER NOT NULL," +
              "quantity INTEGER NOT NULL," +
              "price REAL NOT NULL," +
              "FOREIGN KEY (order_id) REFERENCES Orders(id)," +
              "FOREIGN KEY (food_item_id) REFERENCES FoodItems(id)" +
              ");";
            stmt.execute(orderDetailsTable);

            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            System.out.println("An error occurred while creating tables: " + e.getMessage());
        }
    }
}
