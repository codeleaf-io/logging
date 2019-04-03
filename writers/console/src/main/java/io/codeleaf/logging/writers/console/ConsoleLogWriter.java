package io.codeleaf.logging.writers.console;

import io.codeleaf.logging.LogInvocation;
import io.codeleaf.logging.LoggingException;
import io.codeleaf.logging.spi.LogWriter;

import java.io.PrintStream;
import java.util.Date;

/**
 * Provides <code>LogWriter</code> that writes to the console.
 *
 * @author tvburger@gmail.com
 * @see LogWriter
 * @since 0.1.0
 */
public final class ConsoleLogWriter implements LogWriter {

    private final String logName;
    private final PrintStream out;
    private final PrintStream err;

    /**
     * Constructs a new instance
     *
     * @param logName the name of the log to write
     * @param out     the print stream to use for debug and info messages
     * @param err     the print stream to use for warning and error messages
     */
    public ConsoleLogWriter(String logName, PrintStream out, PrintStream err) {
        this.logName = logName;
        this.out = out;
        this.err = err;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLogName() {
        return logName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeLog(LogInvocation invocation) throws LoggingException {
        String message = format(invocation);
        switch (invocation.getLogLevel()) {
            case DEBUG:
            case INFO:
                out.println(message);
                if (out.checkError()) {
                    throw new LoggingException("Failed to write log!");
                }
                break;
            case WARNING:
            case ERROR:
                err.println(message);
                if (err.checkError()) {
                    throw new LoggingException("Failed to write log!");
                }
                break;
        }
    }

    private String format(LogInvocation invocation) {
        return String.format("%s [%s] <%s> %s@%s %s: %s",
                new Date(invocation.getInvocationTime()),
                invocation.getLogLevel().name(),
                invocation.getLogName(),
                invocation.getSource().getFileName(),
                invocation.getSource().getLineNumber(),
                invocation.getThreadName(),
                invocation.getMessage());
    }

}
