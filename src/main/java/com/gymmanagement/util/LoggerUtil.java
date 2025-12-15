package com.gymmanagement.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Utility class for centralized logging configuration.
 * Provides a singleton Logger instance with file output capability.
 * Lazily initializes the logger on first use and configures it to log to a file.
 */
public class LoggerUtil {

    // Singleton logger instance for the application
    private static final Logger LOGGER = Logger.getLogger("GymAppLogger");
    // Flag to track whether the logger has been initialized
    private static boolean initialized = false;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private LoggerUtil() {
    }

    /**
     * Gets the application's logger instance.
     * Initializes the logger on first call by configuring a FileHandler for log output.
     * Subsequent calls return the same logger instance without re-initialization.
     * 
     * The logger is configured to:
     * - Output logs to "gym-app.log" file (appends to existing file)
     * - Use SimpleFormatter for log message formatting
     * - Log at INFO level and above
     * 
     * @return the application's Logger instance
     */
    public static Logger getLogger() {
        // Lazy initialization: configure logger only once on first call
        if (!initialized) {
            try {
                // Create a FileHandler that appends to the log file
                FileHandler fileHandler = new FileHandler("gym-app.log", true);
                // Set the format for log messages
                fileHandler.setFormatter(new SimpleFormatter());
                // Add the file handler to the logger
                LOGGER.addHandler(fileHandler);
                // Set the logging level to INFO (logs INFO and higher severity messages)
                LOGGER.setLevel(Level.INFO);
                // Mark initialization as complete
                initialized = true;
            } catch (IOException e) {
                // Log initialization error to standard error stream
                System.err.println("Failed to initialize logger file handler: " + e.getMessage());
            }
        }
        return LOGGER;
    }
}
