package io.codeleaf.logging.bindings.console;

import io.codeleaf.logging.LogLevel;
import io.codeleaf.logging.ext.LogBinder;

import java.io.PrintStream;

/**
 * Provides the <code>LogBinder</code> to the console.
 *
 * @author tvburger@gmail.com
 * @see System#out
 * @see System#err
 * @since 0.1.0
 */
public final class ConsoleLogBinder implements LogBinder {

    private boolean bound = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void bind(Listener listener) {
        System.setOut(new PrintStream(LogOutputStream.create(listener, "stdout", LogLevel.INFO), true));
        System.setErr(new PrintStream(LogOutputStream.create(listener, "stderr", LogLevel.ERROR), true));
        bound = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBound() {
        return bound;
    }

}
