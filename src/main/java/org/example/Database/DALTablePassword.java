package org.example.Database;

import org.example.DTOs.DtoPasswordsFromDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class DALTablePassword {
    String url = "jdbc:sqlserver://localhost:1433;databaseName=Passwortmanager";
    String user = "sa";
    String password = "YourStrong!Passw0rd";
    Connection conn = null;
    PreparedStatement pstmt = null;
    Statement statement = null;

    public void dbAccess() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to the database!");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public void writeinDB(byte[] password, String description) {
        dbAccess();
        try {
            String insertSQL = "INSERT INTO Passwords (PASSWORD, DESCRIPTION, DATE) VALUES (?, ?, ?)";

            pstmt = conn.prepareStatement(insertSQL);
            pstmt.setBytes(1, password);
            pstmt.setString(2, description);
            pstmt.setString(3, LocalDate.now().toString());
            pstmt.executeUpdate();

            System.out.println("Data inserted successfully!");
        } catch (SQLException e) {
            System.out.println("Error writing to database: " + e.getMessage());
        } finally {
            closeconnection();
        }
    }

    public List<DtoPasswordsFromDB> getPasswords() {
        List<DtoPasswordsFromDB> dtos = new ArrayList<>();
        dbAccess();
        try {
            statement = conn.createStatement();
            String query = "SELECT * FROM passwords";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                DtoPasswordsFromDB dataset = new DtoPasswordsFromDB();
                dataset.setPasswords(resultSet.getBytes("PASSWORD"));
                dataset.setDescription(resultSet.getString("DESCRIPTION"));
                dataset.setDate(resultSet.getString("DATE"));
                dtos.add(dataset);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving passwords: " + e.getMessage());
        } finally {
            closeconnection();
        }
        return dtos;
    }

    public List<DtoPasswordsFromDB> getSpecificPassword(String description) {
        List<DtoPasswordsFromDB> dtos = new ArrayList<>();
        dbAccess();
        try {
            statement = conn.createStatement();
            String query = "SELECT * FROM passwords WHERE DESCRIPTION = '" + description + "'";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                DtoPasswordsFromDB dataset = new DtoPasswordsFromDB();
                dataset.setPasswords(resultSet.getBytes("PASSWORD"));
                dataset.setDescription(resultSet.getString("DESCRIPTION"));
                dataset.setDate(resultSet.getString("DATE"));
                dtos.add(dataset);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving specific password: " + e.getMessage());
        } finally {
            closeconnection();
        }
        return dtos;
    }

    public void closeconnection() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
        }
    }
}
