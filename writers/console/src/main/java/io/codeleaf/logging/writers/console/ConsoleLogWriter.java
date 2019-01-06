package io.codeleaf.logging.writers.console;

import io.codeleaf.logging.LogInvocation;
import io.codeleaf.logging.LoggingException;
import io.codeleaf.logging.spi.LogWriter;

import java.io.PrintStream;
import java.util.Date;

public final class ConsoleLogWriter implements LogWriter {

    private final String logName;
    private final PrintStream out;
    private final PrintStream err;

    public ConsoleLogWriter(String logName, PrintStream out, PrintStream err) {
        this.logName = logName;
        this.out = out;
        this.err = err;
    }

    @Override
    public String getLogName() {
        return logName;
    }

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
        return String.format("%s [%s] <%s> %s@%s: %s",
                new Date(invocation.getInvocationTime()),
                invocation.getLogLevel().name(),
                invocation.getLogName(),
                invocation.getSource().getFileName(),
                invocation.getSource().getLineNumber(),
                invocation.getMessage());
    }

}
