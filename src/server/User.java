
import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;



public class User extends UnicastRemoteObject implements UserService {
    
    // Declare a serialVersionUID to avoid the warning
    private static final long serialVersionUID = 1L;

    protected User() throws RemoteException{
        System.out.println("Out from here constructor");
    }

    public boolean registerUser(
            String firstName, 
            String lastName,
            String password, 
            String passportNumber, 
            String email, 
            Integer role
    ) throws RemoteException {
        Connection con = null;
        PreparedStatement psCheck = null;
        PreparedStatement psInsert = null;

        try {
            System.out.println("Out from here");
            con = DatabaseConnection.getConnection();

            String checkEmailQuery = "SELECT email FROM Users WHERE email = ?";
            psCheck = con.prepareStatement(checkEmailQuery);
            psCheck.setString(1, email); // Set the email parameter
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                return false; // Email already exists
            }

            String insertQuery = "INSERT INTO Users (first_name, last_name, password, passport_number, email, role) VALUES (?, ?, ?, ?, ?, ?)";
            psInsert = con.prepareStatement(insertQuery);
            psInsert.setString(1, firstName);
            psInsert.setString(2, lastName);
            psInsert.setString(3, password); // Ideally, you should hash the password before inserting
            psInsert.setString(4, passportNumber);
            psInsert.setString(5, email);
            psInsert.setInt(6, role);

            int rowsAffected = psInsert.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (psCheck != null) {
                    psCheck.close();
                }
                if (psInsert != null) {
                    psInsert.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean loginUser(String email, String password) throws RemoteException {
        Connection con = DatabaseConnection.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String getUserQuery = "SELECT * FROM Users WHERE email = ?";
            PreparedStatement ps = con.prepareStatement(getUserQuery);
            ps.setString(1, email);

            rs = ps.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    return true;
                }
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper User class to store user details
    public static class UserHelper {
        private String firstName;
        private String lastName;
        private String password;
        private String passportNumber;
        private String email;
        private Integer role;

        public UserHelper(String firstName, String lastName, String password, String passportNumber, String email, Integer role) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
            this.passportNumber = passportNumber;
            this.email = email;
            this.role = role;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getPassword() {
            return password;
        }

        public String getPassportNumber() {
            return passportNumber;
        }

        public String getEmail() {
            return email;
        }

        public Integer getRole() {
            return role;
        }
    }
}
