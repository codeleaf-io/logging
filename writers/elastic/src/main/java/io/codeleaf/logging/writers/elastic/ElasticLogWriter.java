package io.codeleaf.logging.writers.elastic;

import io.codeleaf.logging.LogInvocation;
import io.codeleaf.logging.LoggingException;
import io.codeleaf.logging.spi.LogWriter;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;

// TODO: add buffer for log statements and use bulk request
public final class ElasticLogWriter implements LogWriter {

    private final Client client;
    private final String index;
    private final String type;
    private final String loggerName;
    private final ElasticLogFormatter formatter;

    public ElasticLogWriter(Client client, String index, String type, String loggerName, ElasticLogFormatter formatter) {
        this.client = client;
        this.index = index;
        this.type = type;
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
            client.index(new IndexRequest(index, type).source(formatter.formatLog(invocation, loggerName)));
        } catch (Exception cause) {
            throw new LoggingException("Failed to log: " + cause.getMessage(), cause);
        }
    }

}
