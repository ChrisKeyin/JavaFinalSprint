package com.gymmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for managing database connections.
 * Provides a centralized point for obtaining JDBC connections to the PostgreSQL database.
 * Uses the JDBC DriverManager to create and manage database connections.
 */
public class DBConnection {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    // PostgreSQL database connection parameters
    private static final String URL = "jdbc:postgresql://localhost:1508/gym-management";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Vader10156$";

    /**
     * Static initializer block that loads the PostgreSQL JDBC driver.
     * Executed once when the class is first loaded.
     * Logs an error if the driver cannot be found.
     */
    static {
        try {
            Class.forName("org.postgresql.Driver");
            LOGGER.info("PostgreSQL JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "PostgreSQL JDBC Driver not found!", e);
        }
    }

    /**
     * Obtains a new database connection to the PostgreSQL database.
     * Each call creates a new connection using the configured credentials.
     * 
     * @return a new Connection object connected to the gym-management database
     * @throws SQLException if a database access error occurs or the URL is invalid
     */
    public static Connection getConnection() throws SQLException {
        LOGGER.fine("Attempting to connect to database: " + URL);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
