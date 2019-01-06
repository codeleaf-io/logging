package io.codeleaf.logging.bindings.console;

import io.codeleaf.logging.LogInvocation;
import io.codeleaf.logging.LogLevel;
import io.codeleaf.logging.spi.LogListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class LogOutputStream extends OutputStream {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private final LogListener listener;
    private final String logName;
    private final LogLevel logLevel;

    public LogOutputStream(LogListener listener, String logName, LogLevel logLevel) {
        this.listener = listener;
        this.logName = logName;
        this.logLevel = logLevel;
    }

    private void doOutput() {
        String outputString = getOutputString();
        if (!outputString.isEmpty()) {
            listener.logInvoked(LogInvocation.create(logName, outputString, logLevel, getSource()));
        }
        outputStream.reset();
    }

    private StackTraceElement getSource() {
        StackTraceElement[] strackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : strackTrace) {
            if (!element.getClassName().startsWith("io.codeleaf.logging")
                    && !element.getClassName().startsWith("java.")
                    && !element.getClassName().startsWith("sun.")) {
                return element;
            }
        }
        return strackTrace[strackTrace.length - 1];
    }

    private String getOutputString() {
        String output = outputStream.toString();
        return output.endsWith("\n") ? output.substring(0, output.length() - 1) : output;
    }

    @Override
    public synchronized void write(int b) {
        outputStream.write(b);
    }

    @Override
    public synchronized void write(byte[] b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        outputStream.write(b, off, len);
    }

    @Override
    public synchronized void flush() {
        if (outputStream.size() > 0) {
            doOutput();
        }
    }

}
