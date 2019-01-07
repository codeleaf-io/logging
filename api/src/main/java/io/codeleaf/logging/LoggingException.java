package io.codeleaf.logging;

/**
 * Thrown when an exception occurred related to logging.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public class LoggingException extends Exception {

    public LoggingException(String message) {
        super(message);
    }

    public LoggingException(String message, Throwable cause) {
        super(message, cause);
    }

}
