package io.codeleaf.logging.bindings.slf4j;

import io.codeleaf.logging.ext.LogBinder;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Slf4jLoggerFactory implements ILoggerFactory {

    private final Map<String, Logger> loggers = new ConcurrentHashMap<>();
    private final LogBinder.Listener listener;

    public Slf4jLoggerFactory(LogBinder.Listener listener) {
        this.listener = listener;
    }

    @Override
    public Logger getLogger(String logName) {
        return loggers.computeIfAbsent(logName, (name) -> new Slf4jLogger(name, listener));
    }

}
