package io.codeleaf.logging.spi;

import io.codeleaf.logging.LogInvocation;
import io.codeleaf.logging.LoggingException;

public interface LogWriter {

    String getLogName() throws LoggingException;

    void writeLog(LogInvocation invocation) throws LoggingException;

}
