package io.codeleaf.logging.writers.elastic;

import io.codeleaf.config.ConfigurationException;
import io.codeleaf.config.ConfigurationProvider;
import io.codeleaf.logging.spi.LogWriter;
import io.codeleaf.logging.spi.LogWriterProvider;
import io.codeleaf.logging.writers.elastic.config.ElasticLogConfiguration;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public final class ElasticLogWriterProvider implements LogWriterProvider {

    private final Map<String, ElasticLogWriter> loggers = new HashMap<>();
    private final ElasticLogFormatter formatter = new ElasticLogFormatter();

    private final Client client;
    private final String indexName;
    private final String typeBaseName;
    private final boolean ensureExists;

    private static Client createClient(ElasticLogConfiguration configuration) {
        return createClient(configuration.getClusterName(), configuration.getClusterAddresses());
    }

    private static Client createClient(String clusterName, TransportAddress[] clusterAddresses) {
        PreBuiltTransportClient client = new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", clusterName)
                .build());
        for (TransportAddress address : clusterAddresses) {
            client.addTransportAddress(address);
        }
        return client;
    }

    public ElasticLogWriterProvider() throws ExecutionException, InterruptedException, ConfigurationException, IOException {
        this(ConfigurationProvider.get().getConfiguration(ElasticLogConfiguration.class));
        init();
    }

    public ElasticLogWriterProvider(ElasticLogConfiguration configuration) {
        this(createClient(configuration),
                configuration.getIndexName(),
                configuration.getTypeBaseName(),
                configuration.isEnsureExists());
    }

    public ElasticLogWriterProvider(Client client, String indexName, String typeBaseName, boolean ensureExists) {
        this.client = client;
        this.indexName = indexName;
        this.typeBaseName = typeBaseName;
        this.ensureExists = ensureExists;
    }

    private void init() throws ExecutionException, InterruptedException {
        if (ensureExists) {
            if (!indexExists()) {
                createIndexAndMapping();
            } else if (!typeExists()) {
                createMapping();
            }
        }
    }

    private boolean indexExists() throws ExecutionException, InterruptedException {
        IndicesExistsRequest request = new IndicesExistsRequest();
        request.indices(indexName);
        return client.admin().indices().exists(request).get().isExists();
    }

    private boolean typeExists() throws ExecutionException, InterruptedException {
        TypesExistsRequest request = new TypesExistsRequest();
        request.indices(indexName);
        request.types(new String[]{getTypeName()});
        return client.admin().indices().typesExists(request).get().isExists();
    }

    private void createIndexAndMapping() throws ExecutionException, InterruptedException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.mapping(getTypeName(), formatter.getMapping());
        client.admin().indices().create(request).get();
    }

    private void createMapping() throws ExecutionException, InterruptedException {
        PutMappingRequest request = new PutMappingRequest();
        request.source(formatter.getMapping());
        request.type("_doc");
        client.admin().indices().putMapping(request).get();
    }

    private String getTypeName() {
        return typeBaseName + "_" + formatter.getFormatVersion();
    }

    @Override
    public LogWriter getLogWriter(String loggerName) {
        return loggers.computeIfAbsent(loggerName, (key) -> new ElasticLogWriter(client, indexName, getTypeName(), key, formatter));
    }

}
