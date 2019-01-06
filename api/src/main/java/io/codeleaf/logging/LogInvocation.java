package io.codeleaf.logging;

import java.util.Objects;

public final class LogInvocation {

    public static LogInvocation create(String logName, String message, LogLevel logLevel, StackTraceElement source) {
        return create(logName, message, logLevel, System.currentTimeMillis(), source);
    }

    public static LogInvocation create(String logName, String message, LogLevel logLevel, long invocationTime, StackTraceElement source) {
        Objects.requireNonNull(logName);
        Objects.requireNonNull(message);
        Objects.requireNonNull(logLevel);
        Objects.requireNonNull(source);
        return new LogInvocation(logName, message, logLevel, invocationTime, source);
    }

    private final String logName;
    private final String message;
    private final LogLevel logLevel;
    private final long invocationTime;
    private final StackTraceElement source;

    private LogInvocation(String logName, String message, LogLevel logLevel, long invocationTime, StackTraceElement source) {
        this.logName = logName;
        this.message = message;
        this.logLevel = logLevel;
        this.invocationTime = invocationTime;
        this.source = source;
    }

    public String getLogName() {
        return logName;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public long getInvocationTime() {
        return invocationTime;
    }

    public StackTraceElement getSource() {
        return source;
    }

}
