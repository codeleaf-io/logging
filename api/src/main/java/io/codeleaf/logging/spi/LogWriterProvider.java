package io.codeleaf.logging.spi;

import io.codeleaf.logging.LoggingException;

public interface LogWriterProvider {

    LogWriter getLogWriter(String logName) throws LoggingException;

}
