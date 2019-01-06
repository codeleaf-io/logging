package io.codeleaf.logging.spi;

import io.codeleaf.logging.LogInvocation;
import io.codeleaf.logging.LoggingException;

/**
 * Interface for providing a new log writer.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public interface LogWriter {

    /**
     * Returns the name of this logger.
     *
     * @return the name of this logger
     */
    String getLogName();

    /**
     * Writes the log invocation to the log
     *
     * @param invocation the invocation of the logging
     * @throws LoggingException if an exception occurred
     */
    void writeLog(LogInvocation invocation) throws LoggingException;

}
