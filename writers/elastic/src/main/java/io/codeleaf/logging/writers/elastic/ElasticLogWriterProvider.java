package io.codeleaf.logging.writers.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import io.codeleaf.common.libs.elastic.ElasticIndexHelper;
import io.codeleaf.common.libs.elastic.ElasticTransportFactory;
import io.codeleaf.config.ConfigurationException;
import io.codeleaf.config.ConfigurationProvider;
import io.codeleaf.logging.spi.LogWriter;
import io.codeleaf.logging.spi.LogWriterProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public final class ElasticLogWriterProvider implements LogWriterProvider {

    private final Map<String, ElasticLogWriter> loggers = new HashMap<>();
    private final ElasticLogFormatter formatter = new ElasticLogFormatter();

    private final ElasticIndexHelper helper;
    private final String indexName;
    private final boolean ensureExists;

    public ElasticLogWriterProvider() throws ExecutionException, InterruptedException, ConfigurationException, IOException {
        this(ConfigurationProvider.get().getConfiguration(ElasticLogConfiguration.class));
        init();
    }

    public ElasticLogWriterProvider(ElasticLogConfiguration configuration) {
        this(new ElasticIndexHelper(
                        new ElasticsearchClient(
                                ElasticTransportFactory.createTransport(configuration)), false),
                configuration.getIndexName(), configuration.isEnsureExists());
    }

    public ElasticLogWriterProvider(ElasticIndexHelper helper, String indexName, boolean ensureExists) {
        this.helper = helper;
        this.indexName = indexName;
        this.ensureExists = ensureExists;
    }

    private void init() throws IOException {
        if (ensureExists) {
            if (!helper.existsIndex(indexName)) {
                helper.createIndex(indexName, formatter.getMapping());
            }
        }
    }

    @Override
    public LogWriter getLogWriter(String loggerName) {
        return loggers.computeIfAbsent(loggerName, (key) -> new ElasticLogWriter(helper, indexName, key, formatter));
    }

}
