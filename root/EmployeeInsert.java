package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.access.DBConnection;

public class EmployeeInsert {

    public static void main(String[] args) {
        try {
        	
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Get connection from DBConnection class
            String url = DBConnection.getUrl();
            String user = DBConnection.getUser();
            String password = DBConnection.getPassword();

            Connection con = DriverManager.getConnection(url, user, password);

            // SQL statements
            String insertQuery = "INSERT INTO employe (empcode, empname, empage, esalary) VALUES (?, ?, ?, ?)";
            String checkQuery = "SELECT COUNT(*) FROM employe WHERE empcode = ?";

            PreparedStatement insertStmt = con.prepareStatement(insertQuery);
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);

            Object[][] employees = {
                {101, "Jenny", 25, 10000},
                {102, "Jacky", 30, 20000},
                {103, "Joe", 20, 40000},
                {104, "John", 40, 80000},
                {105, "Shameer", 25, 90000}
            };

            for (Object[] emp : employees) {
                // Check if empcode already exists
                checkStmt.setInt(1, (Integer) emp[0]);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();

                if (rs.getInt(1) == 0) { // empcode does not exist
                    insertStmt.setInt(1, (Integer) emp[0]);
                    insertStmt.setString(2, (String) emp[1]);
                    insertStmt.setInt(3, (Integer) emp[2]);
                    insertStmt.setDouble(4, ((Integer) emp[3]).doubleValue());
                    insertStmt.executeUpdate(); // insert
                    System.out.println("Inserted: " + emp[1]);
                } else {
                    System.out.println("Skipped duplicate empcode: " + emp[0]);
                }
            }

            // Close resources
            insertStmt.close();
            checkStmt.close();
            con.close();

            System.out.println("Process completed!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
