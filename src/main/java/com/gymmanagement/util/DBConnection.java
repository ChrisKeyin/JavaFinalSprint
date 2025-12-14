package com.gymmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    private static final String URL = "jdbc:postgresql://localhost:1508/gym-management";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Vader10156$";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            LOGGER.info("PostgreSQL JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "PostgreSQL JDBC Driver not found!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        LOGGER.fine("Attempting to connect to database: " + URL);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
