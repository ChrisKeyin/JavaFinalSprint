package com.gymmanagement.app;

import com.gymmanagement.util.DBConnection;
import com.gymmanagement.util.LoggerUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class to test the database connection.
 * This is used to verify that the PostgreSQL database is accessible and properly configured.
 */
public class TestConnection {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    /**
     * Main entry point for testing the database connection.
     * Attempts to establish a connection to the PostgreSQL database and logs the result.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Try to obtain a connection from the DBConnection utility
        try (Connection conn = DBConnection.getConnection()) {
            // Check if connection is successful
            if (conn != null) {
                System.out.println("Connected to PostgreSQL successfully!");
                LOGGER.info("Database connection test succeeded.");
            } else {
                System.out.println("Connection is null.");
                LOGGER.warning("Database connection test returned null connection.");
            }
        } catch (SQLException e) {
            // Handle connection errors
            System.out.println("Failed to connect to PostgreSQL. See log for details.");
            LOGGER.log(Level.SEVERE, "Failed to connect to PostgreSQL during TestConnection.", e);
        }
    }
}
