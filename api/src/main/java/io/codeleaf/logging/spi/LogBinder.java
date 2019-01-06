package io.codeleaf.logging.spi;

import io.codeleaf.logging.LogInvocation;
import io.codeleaf.logging.LoggingException;

/**
 * Provides the service provider interface for defining a new logging binding.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public interface LogBinder {

    /**
     * Bind the log to the specified listener. Will be invoked only once for each implementation.
     * The implementation of this class should call {@link LogListener#logInvoked(LogInvocation)} for
     * each log invocation.
     *
     * @param listener the listener to bind the log invocations to
     * @throws LoggingException if an exception happened
     */
    void bind(LogListener listener) throws LoggingException;

    /**
     * Returns <code>true</code> after <code>bind</code> has been called, otherwise <code>false</code>
     *
     * @return <code>true</code> after <code>bind</code> has been called, otherwise <code>false</code>
     */
    boolean isBound();

}
