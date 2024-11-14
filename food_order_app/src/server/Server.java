package server;

import java.sql.Connection;

public class Server {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            // Create connection
            conn = DataConnection.getConnection();
            if (conn != null) {
                DatabaseInitializer.createTables();
                System.out.println("Connection successful from server");
            } else {
                System.err.println("Failed to establish connection.");
            }
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
        } finally {
            
        	DataConnection.closeConnection(conn);
        }
    }
}
