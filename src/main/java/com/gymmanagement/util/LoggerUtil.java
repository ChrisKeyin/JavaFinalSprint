package com.gymmanagement.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Utility class for configuring and providing a shared application-wide {@link Logger}.
 * <p>
 * The logger is configured to write messages to a text file named {@code gym-app.log}.
 * This allows us to keep a persistent record of important events and errors.
 */
public class LoggerUtil {

    private static final Logger LOGGER = Logger.getLogger("GymAppLogger");
    private static boolean initialized = false;

    private LoggerUtil() {
        // utility class
    }

    /**
     * Returns the shared application logger. On first use, this method configures
     * the logger with a {@link FileHandler} that writes to {@code gym-app.log}.
     *
     * @return the configured {@link Logger} instance
     */
    public static Logger getLogger() {
        if (!initialized) {
            try {
                // Append mode: logs go to gym-app.log in the working directory
                FileHandler fileHandler = new FileHandler("gym-app.log", true);
                fileHandler.setFormatter(new SimpleFormatter());
                LOGGER.addHandler(fileHandler);
                LOGGER.setLevel(Level.INFO);
                initialized = true;
            } catch (IOException e) {
                // As a last resort, log to stderr if logger fails
                System.err.println("Failed to initialize logger file handler: " + e.getMessage());
            }
        }
        return LOGGER;
    }
}
