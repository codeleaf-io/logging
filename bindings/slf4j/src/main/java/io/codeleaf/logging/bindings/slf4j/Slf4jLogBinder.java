package io.codeleaf.logging.bindings.slf4j;

import io.codeleaf.logging.ext.LogBinder;
import org.slf4j.impl.StaticLoggerBinder;

public final class Slf4jLogBinder implements LogBinder {

    @Override
    public void bind(Listener listener) {
        StaticLoggerBinder.getSingleton().getLogListener().set(listener);
    }

    @Override
    public boolean isBound() {
        return StaticLoggerBinder.getSingleton().getLogListener().get() != null;
    }

}
