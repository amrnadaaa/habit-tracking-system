package com.myapp.service;

import com.myapp.model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    private Connection connection;

    public AuthService(Connection connection) {
        this.connection = connection;
    }

    public boolean signUp(String email, String password) throws SQLException {

        String checkQuery = "SELECT id FROM user WHERE email = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.out.println(" Email already exists!");
                return false;
            }
        }


        String insertQuery = "INSERT INTO user (email, password) VALUES (?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, email);
            insertStmt.setString(2, password);
            insertStmt.executeUpdate();
            System.out.println(" User signed up successfully!");
            return true;
        }
    }

    public boolean login(String email, String password) throws SQLException {
        String query = "SELECT password FROM user WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    System.out.println(" Login successful!");
                    return true;
                } else {
                    System.out.println(" Wrong password!");
                    return false;
                }
            } else {
                System.out.println(" Email not found!");
                return false;
            }
        }
    }
}
