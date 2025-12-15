package com.gymmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class responsible for creating JDBC connections to the PostgreSQL database.
 * <p>
 * The database URL, username, and password are configured as constants in this class.
 * Other parts of the application should always obtain a connection via {@link #getConnection()}.
 */
public class DBConnection {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    // TODO: change these to your actual PostgreSQL details
    private static final String URL = "jdbc:postgresql://localhost:5432/gym_management_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "your_password_here";

    static {
        try {
            // Optional for modern JDBC but safe:
            Class.forName("org.postgresql.Driver");
            LOGGER.info("PostgreSQL JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "PostgreSQL JDBC Driver not found!", e);
        }
    }

    /**
     * Creates and returns a new database connection using the configured URL, username, and password.
     *
     * @return a new {@link Connection} instance
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        LOGGER.fine("Attempting to connect to database: " + URL);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
