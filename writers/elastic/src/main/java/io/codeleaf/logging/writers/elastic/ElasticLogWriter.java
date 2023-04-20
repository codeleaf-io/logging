package io.codeleaf.logging.writers.elastic;

import io.codeleaf.common.libs.elastic.ElasticIndexHelper;
import io.codeleaf.logging.LogInvocation;
import io.codeleaf.logging.LoggingException;
import io.codeleaf.logging.spi.LogWriter;

// TODO: add buffer for log statements and use bulk request
public final class ElasticLogWriter implements LogWriter {

    private final ElasticIndexHelper helper;
    private final String index;
    private final String loggerName;
    private final ElasticLogFormatter formatter;

    public ElasticLogWriter(ElasticIndexHelper helper, String index, String loggerName, ElasticLogFormatter formatter) {
        this.helper = helper;
        this.index = index;
        this.loggerName = loggerName;
        this.formatter = formatter;
    }

    @Override
    public String getLogName() {
        return loggerName;
    }

    @Override
    public void writeLog(LogInvocation invocation) throws LoggingException {
        try {
            helper.indexDocument(index, formatter.formatLog(invocation, loggerName));
        } catch (Exception cause) {
            throw new LoggingException("Failed to log: " + cause.getMessage(), cause);
        }
    }

}
