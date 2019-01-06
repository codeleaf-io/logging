package io.codeleaf.logging.writers.elastic.config;

import io.codeleaf.config.Configuration;
import org.elasticsearch.common.transport.TransportAddress;

public final class ElasticLogConfiguration implements Configuration {

    private final String clusterName;
    private final TransportAddress[] clusterAddresses;
    private final String indexName;
    private final String typeBaseName;
    private final boolean ensureExists;

    public ElasticLogConfiguration(String clusterName, TransportAddress[] clusterAddresses, String indexName, String typeBaseName, boolean ensureExists) {
        this.clusterName = clusterName;
        this.clusterAddresses = clusterAddresses;
        this.indexName = indexName;
        this.typeBaseName = typeBaseName;
        this.ensureExists = ensureExists;
    }

    public String getClusterName() {
        return clusterName;
    }

    public TransportAddress[] getClusterAddresses() {
        return clusterAddresses;
    }

    public String getIndexName() {
        return indexName;
    }

    public String getTypeBaseName() {
        return typeBaseName;
    }

    public boolean isEnsureExists() {
        return ensureExists;
    }
}
