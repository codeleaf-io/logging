package io.codeleaf.logging.writers.console;

import io.codeleaf.logging.core.LogBindings;
import io.codeleaf.logging.spi.LogWriter;
import io.codeleaf.logging.spi.LogWriterProvider;

/**
 * Provides the log writers to the console.
 *
 * @author tvburger@gmail.com
 * @see LogWriterProvider
 * @since 0.1.0
 */
public final class ConsoleLogWriterProvider implements LogWriterProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public LogWriter getLogWriter(String logName) {
        return new ConsoleLogWriter(logName, LogBindings.getOriginalOut(), LogBindings.getOriginalErr());
    }

}
