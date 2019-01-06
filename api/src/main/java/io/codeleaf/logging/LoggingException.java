package io.codeleaf.logging;

public class LoggingException extends Exception {

    public LoggingException(String message) {
        super(message);
    }

    public LoggingException(String message, Throwable cause) {
        super(message, cause);
    }

}
