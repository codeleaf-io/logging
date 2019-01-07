package org.slf4j.impl;

import io.codeleaf.logging.bindings.slf4j.Slf4jLoggerFactory;
import io.codeleaf.logging.impl.SettableLogListener;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public final class StaticLoggerBinder implements LoggerFactoryBinder {

    public static final String REQUESTED_API_VERSION = "1.7";
    private static final String LOGGER_FACTORY_CLASS_STR = Slf4jLoggerFactory.class.getName();
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    private final SettableLogListener listener = new SettableLogListener();
    private final Slf4jLoggerFactory loggerFactory = new Slf4jLoggerFactory(listener);

    private StaticLoggerBinder() {
    }

    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    public ILoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }

    public String getLoggerFactoryClassStr() {
        return LOGGER_FACTORY_CLASS_STR;
    }

    public SettableLogListener getLogListener() {
        return listener;
    }

}
