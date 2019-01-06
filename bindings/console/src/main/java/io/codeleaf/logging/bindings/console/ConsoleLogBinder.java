package io.codeleaf.logging.bindings.console;

import io.codeleaf.logging.LogLevel;
import io.codeleaf.logging.spi.LogBinder;
import io.codeleaf.logging.spi.LogListener;

import java.io.PrintStream;

public final class ConsoleLogBinder implements LogBinder {

    private boolean bound = false;

    @Override
    public void bind(LogListener listener) {
        if (isBound()) {
            throw new IllegalStateException("Already bound!");
        }
        System.setOut(new PrintStream(new LogOutputStream(listener, "stdout", LogLevel.INFO), true));
        System.setErr(new PrintStream(new LogOutputStream(listener, "stderr", LogLevel.ERROR), true));
        bound = true;
    }

    @Override
    public boolean isBound() {
        return bound;
    }

}
