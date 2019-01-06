package io.codeleaf.logging.spi;

import io.codeleaf.logging.LogInvocation;

/**
 * The listener interface for receiving log invocations.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public interface LogListener {

    /**
     * Invoked when a log invocation happened.
     *
     * @param invocation the invocation of the log
     */
    void logInvoked(LogInvocation invocation);

}
