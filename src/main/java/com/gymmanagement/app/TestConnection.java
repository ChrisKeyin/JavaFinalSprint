package com.gymmanagement.app;

import com.gymmanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connected to PostgreSQL successfully!");
            } else {
                System.out.println("Connection is null.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to PostgreSQL:");
            e.printStackTrace();
        }
    }
}
