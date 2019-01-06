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

public final class LoggingBindings {

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

    public static void init() {
        LoggingBindings bindings = new LoggingBindings();
        bindings.loadWriters();
        bindings.loadBindings();
    }

    public static PrintStream getOriginalOut() {
        return out;
    }

    public static PrintStream getOriginalErr() {
        return err;
    }

    private LoggingBindings() {
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
