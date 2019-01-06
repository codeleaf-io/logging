package io.codeleaf.logging.bindings.console;

import io.codeleaf.logging.LogInvocation;
import io.codeleaf.logging.LogLevel;
import io.codeleaf.logging.spi.LogListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Implements an <code>OutputStream</code> that forwards the messages to a <code>LogListener</code>.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class LogOutputStream extends OutputStream {

    /**
     * Creates a new instance.
     *
     * @param listener the listener to forward the messages to
     * @param logName  the name of the logger to use
     * @param logLevel the log level to use
     * @return a new instance
     * @throws NullPointerException if <code>listener</code>, <code>logName</code>, or <code>logLevel</code> is null.
     */
    public static LogOutputStream create(LogListener listener, String logName, LogLevel logLevel) {
        Objects.requireNonNull(listener);
        Objects.requireNonNull(logName);
        Objects.requireNonNull(logLevel);
        return new LogOutputStream(listener, logName, logLevel);
    }

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private final LogListener listener;
    private final String logName;
    private final LogLevel logLevel;

    private LogOutputStream(LogListener listener, String logName, LogLevel logLevel) {
        this.listener = listener;
        this.logName = logName;
        this.logLevel = logLevel;
    }

    private void doOutput() {
        String outputString = getOutputString();
        if (!outputString.isEmpty()) {
            listener.logInvoked(LogInvocation.create(logName, outputString, logLevel));
        }
        outputStream.reset();
    }

    private String getOutputString() {
        String output = outputStream.toString();
        return output.endsWith("\n") ? output.substring(0, output.length() - 1) : output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void write(int b) {
        outputStream.write(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void write(byte[] b) throws IOException {
        outputStream.write(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void write(byte[] b, int off, int len) {
        outputStream.write(b, off, len);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void flush() {
        if (outputStream.size() > 0) {
            doOutput();
        }
    }

}
