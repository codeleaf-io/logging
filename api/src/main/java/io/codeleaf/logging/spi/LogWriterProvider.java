package io.codeleaf.logging.spi;

import io.codeleaf.logging.LoggingException;

/**
 * The service provider interface for providing log writers.
 *
 * @author tvburger@gmail.com
 * @see LogWriter
 * @since 0.1.0
 */
public interface LogWriterProvider {

    /**
     * Returns the log writer for the specified log.
     *
     * @param logName the name of the log
     * @return the log writer for the specified log
     * @throws LoggingException if an exception occurred
     */
    LogWriter getLogWriter(String logName) throws LoggingException;

}
