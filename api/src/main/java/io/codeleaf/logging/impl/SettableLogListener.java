package io.codeleaf.logging.impl;

import io.codeleaf.logging.LogInvocation;
import io.codeleaf.logging.ext.LogBinder;

import java.util.function.Supplier;

/**
 * A log listener that can be set at a later moment.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class SettableLogListener implements LogBinder.Listener, Supplier<LogBinder.Listener> {

    private LogBinder.Listener listener;

    /**
     * Constructs a new instance that has no Listener bound. Until set, acts as a NOP.
     */
    public SettableLogListener() {
        this(null);
    }

    /**
     * Constructs a new instance with the provided listener.
     *
     * @param listener the listener to invoke when {@link #logInvoked(LogInvocation)} is called
     */
    public SettableLogListener(LogBinder.Listener listener) {
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logInvoked(LogInvocation invocation) {
        if (listener != null) {
            listener.logInvoked(invocation);
        }
    }

    /**
     * Returns the listener that is invoked, or <code>null</code> if none used
     *
     * @return the listener that is invoked, or <code>null</code> if none used
     */
    @Override
    public LogBinder.Listener get() {
        return listener;
    }

    /**
     * Sets the listener to invoke  when {@link #logInvoked(LogInvocation)} is called
     *
     * @param listener the listener to invoke  when {@link #logInvoked(LogInvocation)} is called
     */
    public void set(LogBinder.Listener listener) {
        this.listener = listener;
    }

}
