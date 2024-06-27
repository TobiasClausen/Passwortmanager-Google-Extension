package org.example.Database;

import org.example.DTOs.DtoPasswordsFromDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;


public class DALTablePassword {
    String url = "jdbc:sqlserver://localhost:1433;databaseName=Passwortmanager";
    String user = "sa";
    String password = "YourStrong!Passw0rd";
    Connection conn = null;
    PreparedStatement pstmt = null;
    Statement statement = null;


    public void dbAccess() {
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void writeinDB(byte[] password, String description) {
        try {
            dbAccess();
            String insertSQL = "INSERT INTO Passwords VALUES (?, ?, ?)";

            pstmt = conn.prepareStatement(insertSQL);
            pstmt.setBytes(1, password);
            pstmt.setString(2, description);
            pstmt.setString(3, LocalDate.now().toString());
            pstmt.executeUpdate();

            System.out.println("Data inserted successfully!");
            closeconnection();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public List<DtoPasswordsFromDB> getPasswords(List<DtoPasswordsFromDB> passwords) throws SQLException {
        List<DtoPasswordsFromDB> dtos = passwords;
        dbAccess();
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
        closeconnection();
        return dtos;
    }
    public List<DtoPasswordsFromDB> getSpecificPassword(String description, List<DtoPasswordsFromDB> dtos) {
        try {
            dbAccess();
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
            closeconnection();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return dtos;
    }

    public void closeconnection(){
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

