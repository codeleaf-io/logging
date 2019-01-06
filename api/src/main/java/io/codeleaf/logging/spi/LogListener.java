package io.codeleaf.logging.spi;

import io.codeleaf.logging.LogInvocation;

public interface LogListener {

    void logInvoked(LogInvocation invocation);

}
