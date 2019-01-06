package io.codeleaf.logging.core;

import io.codeleaf.logging.LoggingException;
import io.codeleaf.logging.spi.LogBinder;
import io.codeleaf.logging.spi.LogListener;
import io.codeleaf.logging.spi.LogWriter;
import io.codeleaf.logging.spi.LogWriterProvider;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Enables the logging system.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class LogBindings {

    private static final PrintStream out = System.out;
    private static final PrintStream err = System.err;

    private final Map<LogWriterProvider, Map<String, LogWriter>> providers = new HashMap<>();

    private final LogListener listener = invocation -> {
        for (Map.Entry<LogWriterProvider, Map<String, LogWriter>> entry : providers.entrySet()) {
            try {
                Map<String, LogWriter> writers = entry.getValue();
                LogWriter writer = writers.get(invocation.getLogName());
                if (writer == null) {
                    LogWriterProvider provider = entry.getKey();
                    writer = provider.getLogWriter(invocation.getLogName());
                    writers.put(invocation.getLogName(), writer);
                }
                writer.writeLog(invocation);
            } catch (LoggingException cause) {
                err.println(String.format(
                        "Failed to log to %s with level %s, reason: %s",
                        entry.getKey().getClass().getName(),
                        invocation.getLogName(),
                        cause.getMessage()));
            }
        }
    };

    /**
     * Initializes the logging system.
     */
    public static void init() {
        LogBindings bindings = new LogBindings();
        bindings.loadWriters();
        bindings.loadBindings();
    }

    /**
     * Returns the {@link System#out} before the logging system was initialized.
     *
     * @return the {@link System#out} before the logging system was initialized
     */
    public static PrintStream getOriginalOut() {
        return out;
    }

    /**
     * Returns the {@link System#err} before the logging system was initialized.
     *
     * @return the {@link System#err} before the logging system was initialized
     */
    public static PrintStream getOriginalErr() {
        return err;
    }

    private LogBindings() {
    }

    private void loadWriters() {
        for (LogWriterProvider provider : ServiceLoader.load(LogWriterProvider.class)) {
            providers.put(provider, new HashMap<>());
            out.println("Loaded " + provider.getClass().getName());
        }
    }

    private void loadBindings() {
        for (LogBinder logBinder : ServiceLoader.load(LogBinder.class)) {
            try {
                if (logBinder.isBound()) {
                    throw new LoggingException("Already bound!");
                }
                logBinder.bind(listener);
                out.println("Bound " + logBinder.getClass().getName());
            } catch (LoggingException cause) {
                err.println("Failed to bind " + logBinder.getClass().getName() + ": " + cause.getCause());
            }
        }
    }

}
