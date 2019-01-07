package io.codeleaf.logging.bindings.slf4j;

import io.codeleaf.logging.LogInvocation;
import io.codeleaf.logging.LogLevel;
import io.codeleaf.logging.ext.LogBinder;
import org.slf4j.Logger;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class Slf4jLogger extends MarkerIgnoringBase implements Logger {

    private final String logName;
    private final LogBinder.Listener listener;

    public Slf4jLogger(String logName, LogBinder.Listener listener) {
        this.logName = logName;
        this.listener = listener;
    }

    private void log(LogLevel level, String message, Throwable t) {
        long invocationTime = System.currentTimeMillis();
        listener.logInvoked(LogInvocation.create(logName, message, level, invocationTime));
        if (t != null) {
            logStackTrace(level, t, invocationTime);
        }
    }

    private void logStackTrace(LogLevel level, Throwable t, long invocationTime) {
        try (StringWriter stringWriter = new StringWriter(); PrintWriter printWriter = new PrintWriter(stringWriter)) {
            t.printStackTrace(printWriter);
            String stackTraceMessage = stringWriter.getBuffer().toString();
            listener.logInvoked(LogInvocation.create(logName, stackTraceMessage, level, invocationTime));
        } catch (IOException cause) {
            throw new IllegalStateException(cause);
        }
    }

    private void formatAndLog(LogLevel level, String format, Object arg1, Object arg2) {
        if (this.isLevelEnabled(level)) {
            FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
            this.log(level, tp.getMessage(), tp.getThrowable());
        }
    }

    private void formatAndLog(LogLevel level, String format, Object... arguments) {
        if (this.isLevelEnabled(level)) {
            FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
            this.log(level, tp.getMessage(), tp.getThrowable());
        }
    }

    private boolean isLevelEnabled(LogLevel level) {
        return true;
    }

    public boolean isTraceEnabled() {
        return this.isLevelEnabled(LogLevel.DEBUG);
    }

    public void trace(String msg) {
        this.log(LogLevel.DEBUG, msg, null);
    }

    public void trace(String format, Object param1) {
        this.formatAndLog(LogLevel.DEBUG, format, param1, null);
    }

    public void trace(String format, Object param1, Object param2) {
        this.formatAndLog(LogLevel.DEBUG, format, param1, param2);
    }

    public void trace(String format, Object... argArray) {
        this.formatAndLog(LogLevel.DEBUG, format, argArray);
    }

    public void trace(String msg, Throwable t) {
        this.log(LogLevel.DEBUG, msg, t);
    }

    public boolean isDebugEnabled() {
        return this.isLevelEnabled(LogLevel.DEBUG);
    }

    public void debug(String msg) {
        this.log(LogLevel.DEBUG, msg, null);
    }

    public void debug(String format, Object param1) {
        this.formatAndLog(LogLevel.DEBUG, format, param1, null);
    }

    public void debug(String format, Object param1, Object param2) {
        this.formatAndLog(LogLevel.DEBUG, format, param1, param2);
    }

    public void debug(String format, Object... argArray) {
        this.formatAndLog(LogLevel.DEBUG, format, argArray);
    }

    public void debug(String msg, Throwable t) {
        this.log(LogLevel.DEBUG, msg, t);
    }

    public boolean isInfoEnabled() {
        return this.isLevelEnabled(LogLevel.INFO);
    }

    public void info(String msg) {
        this.log(LogLevel.INFO, msg, null);
    }

    public void info(String format, Object arg) {
        this.formatAndLog(LogLevel.INFO, format, arg, null);
    }

    public void info(String format, Object arg1, Object arg2) {
        this.formatAndLog(LogLevel.INFO, format, arg1, arg2);
    }

    public void info(String format, Object... argArray) {
        this.formatAndLog(LogLevel.INFO, format, argArray);
    }

    public void info(String msg, Throwable t) {
        this.log(LogLevel.INFO, msg, t);
    }

    public boolean isWarnEnabled() {
        return this.isLevelEnabled(LogLevel.WARNING);
    }

    public void warn(String msg) {
        this.log(LogLevel.WARNING, msg, null);
    }

    public void warn(String format, Object arg) {
        this.formatAndLog(LogLevel.WARNING, format, arg, null);
    }

    public void warn(String format, Object arg1, Object arg2) {
        this.formatAndLog(LogLevel.WARNING, format, arg1, arg2);
    }

    public void warn(String format, Object... argArray) {
        this.formatAndLog(LogLevel.WARNING, format, argArray);
    }

    public void warn(String msg, Throwable t) {
        this.log(LogLevel.WARNING, msg, t);
    }

    public boolean isErrorEnabled() {
        return this.isLevelEnabled(LogLevel.ERROR);
    }

    public void error(String msg) {
        this.log(LogLevel.ERROR, msg, null);
    }

    public void error(String format, Object arg) {
        this.formatAndLog(LogLevel.ERROR, format, arg, null);
    }

    public void error(String format, Object arg1, Object arg2) {
        this.formatAndLog(LogLevel.ERROR, format, arg1, arg2);
    }

    public void error(String format, Object... argArray) {
        this.formatAndLog(LogLevel.ERROR, format, argArray);
    }

    public void error(String msg, Throwable t) {
        this.log(LogLevel.ERROR, msg, t);
    }

    private static LogLevel getLevel(int i) {
        LogLevel logLevel;
        if (i < 20) {
            logLevel = LogLevel.DEBUG;
        } else if (i < 30) {
            logLevel = LogLevel.INFO;
        } else if (i < 40) {
            logLevel = LogLevel.WARNING;
        } else {
            logLevel = LogLevel.ERROR;
        }
        return logLevel;
    }

}
