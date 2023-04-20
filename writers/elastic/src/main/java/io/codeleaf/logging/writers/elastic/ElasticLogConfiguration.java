package io.codeleaf.logging.writers.elastic;

import io.codeleaf.common.libs.elastic.ElasticClientSettings;
import io.codeleaf.config.Configuration;

public final class ElasticLogConfiguration extends ElasticClientSettings implements Configuration {

    private final String indexName;
    private final boolean ensureExists;

    public ElasticLogConfiguration(String host, int port, String fingerprint, String user, String password, String scheme, String indexName, boolean ensureExists) {
        super(host, port, fingerprint, user, password, scheme);
        this.indexName = indexName;
        this.ensureExists = ensureExists;
    }

    public String getIndexName() {
        return indexName;
    }

    public boolean isEnsureExists() {
        return ensureExists;
    }
}
