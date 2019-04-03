package io.codeleaf.logging;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Represents an invocation to a logger.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class LogInvocation {

    /**
     * Creates a new instance. The invocation time is retrieved using <code>System.currentTimeMillis</code>.
     * The source is determined by the first <code>StackTraceElement</code> which class name does not start with
     * <code>java.</code>, <code>sun.</code>, or <code>io.codeleaf.logging.</code>.
     *
     * @param logName  the name of the logger
     * @param message  the message to log
     * @param logLevel the log level to use
     * @return a new instance
     * @throws NullPointerException if <code>logName</code>, <code>message</code>, or <code>logLevel</code> is <code>null</code>
     * @see System#currentTimeMillis()
     * @see Thread#getStackTrace()
     */
    public static LogInvocation create(String logName, String message, LogLevel logLevel) {
        return create(logName, message, logLevel, determineSource());
    }

    private static StackTraceElement determineSource() {
        StackTraceElement[] strackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : strackTrace) {
            if (!element.getClassName().startsWith("io.codeleaf.logging.")
                    && !element.getClassName().startsWith("java.")
                    && !element.getClassName().startsWith("sun.")) {
                return element;
            }
        }
        return strackTrace[strackTrace.length - 1];
    }

    /**
     * Creates a new instance. The source is determined by the first <code>StackTraceElement</code> which class name
     * does not start with <code>java.</code>, <code>sun.</code>, or <code>io.codeleaf.logging.</code>.
     *
     * @param logName        the name of the logger
     * @param message        the message to log
     * @param logLevel       the log level to use
     * @param invocationTime invocation time, the format as provided by {@link System#currentTimeMillis()}
     * @return a new instance
     * @throws NullPointerException if <code>logName</code>, <code>message</code>, or <code>logLevel</code> is <code>null</code>
     * @see Thread#getStackTrace()
     */
    public static LogInvocation create(String logName, String message, LogLevel logLevel, long invocationTime) {
        return create(logName, message, logLevel, invocationTime, determineSource());
    }

    /**
     * Creates a new instance. The invocation time is retrieved using <code>System.currentTimeMillis</code>.
     *
     * @param logName  the name of the logger
     * @param message  the message to log
     * @param logLevel the log level to use
     * @param source   the source of the invocation
     * @return a new instance
     * @throws NullPointerException if <code>logName</code>, <code>message</code>, <code>logLevel</code>, or <code>source</code> is <code>null</code>
     * @see System#currentTimeMillis()
     */
    public static LogInvocation create(String logName, String message, LogLevel logLevel, StackTraceElement source) {
        return create(logName, message, logLevel, System.currentTimeMillis(), source);
    }

    /**
     * Creates a new instance.
     *
     * @param logName        the name of the logger
     * @param message        the message to log
     * @param logLevel       the log level to use
     * @param invocationTime invocation time, the format as provided by {@link System#currentTimeMillis()}
     * @param source         the source of the invocation
     * @return a new instance
     * @throws NullPointerException if <code>logName</code>, <code>message</code>, <code>logLevel</code>, or <code>source</code> is <code>null</code>
     */
    public static LogInvocation create(String logName, String message, LogLevel logLevel, long invocationTime, StackTraceElement source) {
        Objects.requireNonNull(logName);
        Objects.requireNonNull(message);
        Objects.requireNonNull(logLevel);
        Objects.requireNonNull(source);
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException cause) {
            hostName = "unknown";
        }
        String threadName = String.format("%s [%s-%s]",
                Thread.currentThread().getName(), Thread.currentThread().getThreadGroup(), Thread.currentThread().getId());
        return new LogInvocation(logName, message, logLevel, invocationTime, source, hostName, threadName);
    }

    /**
     * Creates a new instance.
     *
     * @param logName        the name of the logger
     * @param message        the message to log
     * @param logLevel       the log level to use
     * @param invocationTime invocation time, the format as provided by {@link System#currentTimeMillis()}
     * @param source         the source of the invocation
     * @param hostName       the name of the host that is logging
     * @param threadName     the name of the thread that is logging
     * @return a new instance
     * @throws NullPointerException if <code>logName</code>, <code>message</code>, <code>logLevel</code>, or <code>source</code> is <code>null</code>
     */
    public static LogInvocation create(String logName, String message, LogLevel logLevel, long invocationTime, StackTraceElement source, String hostName, String threadName) {
        Objects.requireNonNull(logName);
        Objects.requireNonNull(message);
        Objects.requireNonNull(logLevel);
        Objects.requireNonNull(source);
        Objects.requireNonNull(hostName);
        Objects.requireNonNull(threadName);
        return new LogInvocation(logName, message, logLevel, invocationTime, source, hostName, threadName);
    }

    private final String logName;
    private final String message;
    private final LogLevel logLevel;
    private final long invocationTime;
    private final StackTraceElement source;
    private final String hostName;
    private final String threadName;

    private LogInvocation(String logName, String message, LogLevel logLevel, long invocationTime, StackTraceElement source, String hostName, String threadName) {
        this.logName = logName;
        this.message = message;
        this.logLevel = logLevel;
        this.invocationTime = invocationTime;
        this.source = source;
        this.hostName = hostName;
        this.threadName = threadName;
    }

    /**
     * Returns the name of the logger
     *
     * @return the name of the logger
     */
    public String getLogName() {
        return logName;
    }

    /**
     * Returns the message to log
     *
     * @return the message to log
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the log level used
     *
     * @return the log level used
     */
    public LogLevel getLogLevel() {
        return logLevel;
    }

    /**
     * Returns the invocation time of the log invocation, same format as {@link System#currentTimeMillis()}
     *
     * @return the invocation time of the log invocation
     */
    public long getInvocationTime() {
        return invocationTime;
    }

    /**
     * Returns the source of the log invocation
     *
     * @return the source of the log invocation
     */
    public StackTraceElement getSource() {
        return source;
    }

    /**
     * Returns the host name from where the log was written
     *
     * @return the host name from where the log was written
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Returns the thread name from where the log was written
     * @returnm the thread name from where the log was written
     */
    public String getThreadName() {
        return threadName;
    }
}
