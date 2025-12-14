package com.gymmanagement.app;

import com.gymmanagement.util.DBConnection;
import com.gymmanagement.util.LoggerUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestConnection {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connected to PostgreSQL successfully!");
                LOGGER.info("Database connection test succeeded.");
            } else {
                System.out.println("Connection is null.");
                LOGGER.warning("Database connection test returned null connection.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to PostgreSQL. See log for details.");
            LOGGER.log(Level.SEVERE, "Failed to connect to PostgreSQL during TestConnection.", e);
        }
    }
}
