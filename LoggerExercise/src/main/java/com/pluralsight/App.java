package com.pluralsight;

// Import the LogManager and Logger classes from the Log4j library
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Declare a public class named App
public class App {

    // Create a static logger instance for this class
    final static Logger logger = LogManager.getLogger(App.class);

    // The main method — entry point of the program
    public static void main(String[] args) {

        // Call the method 'logMeLikeYouDo' and pass a string to it
        logMeLikeYouDo("☕");  // Example input: coffee cup emoji
    }

    // Define a private static method that accepts a string as input
    private static void logMeLikeYouDo(String input) {

        // Check if debug logging is enabled
        if (logger.isDebugEnabled()) {
            // If so, log a debug message including the input
            logger.debug("This is debug : " + input);
        }

        // Check if info logging is enabled
        if (logger.isInfoEnabled()) {
            // If so, log an info message including the input
            logger.info("This is info : " + input);
        }

        // Log a warning message regardless of settings
        logger.warn("This is warn : " + input);

        // Log an error message regardless of settings
        logger.error("This is error : " + input);

        // Log a fatal message regardless of settings
        logger.fatal("This is fatal : " + input);
    }
}

