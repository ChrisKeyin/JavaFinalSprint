package com.gymmanagement.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {

    private static final Logger LOGGER = Logger.getLogger("GymAppLogger");
    private static boolean initialized = false;

    private LoggerUtil() {
    }

    public static Logger getLogger() {
        if (!initialized) {
            try {
                FileHandler fileHandler = new FileHandler("gym-app.log", true);
                fileHandler.setFormatter(new SimpleFormatter());
                LOGGER.addHandler(fileHandler);
                LOGGER.setLevel(Level.INFO);
                initialized = true;
            } catch (IOException e) {
                System.err.println("Failed to initialize logger file handler: " + e.getMessage());
            }
        }
        return LOGGER;
    }
}
