package io.codeleaf.logging.spi;

import io.codeleaf.logging.LoggingException;

public interface LogBinder {

    void bind(LogListener listener) throws LoggingException;

    boolean isBound();

}
