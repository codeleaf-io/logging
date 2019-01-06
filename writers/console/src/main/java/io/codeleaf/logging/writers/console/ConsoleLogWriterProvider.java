package io.codeleaf.logging.writers.console;

import io.codeleaf.logging.core.LoggingBindings;
import io.codeleaf.logging.spi.LogWriter;
import io.codeleaf.logging.spi.LogWriterProvider;

public final class ConsoleLogWriterProvider implements LogWriterProvider {

    @Override
    public LogWriter getLogWriter(String logName) {
        return new ConsoleLogWriter(logName, LoggingBindings.getOriginalOut(), LoggingBindings.getOriginalErr());
    }

}
