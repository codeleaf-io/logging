package io.codeleaf.logging.writers.elastic.config;

import io.codeleaf.config.impl.AbstractConfigurationFactory;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.Specification;
import org.elasticsearch.common.transport.TransportAddress;

import java.net.InetAddress;

public final class ElasticLogConfigurationFactory extends AbstractConfigurationFactory<ElasticLogConfiguration> {

    private static final ElasticLogConfiguration defaultConfiguration = new ElasticLogConfiguration(
            "docker-cluster",
            new TransportAddress[]{new TransportAddress(InetAddress.getLoopbackAddress(), 9300)},
            "codeleaf",
            "logs",
            true);

    public ElasticLogConfigurationFactory() {
        super(defaultConfiguration);
    }

    @Override
    protected ElasticLogConfiguration parseConfiguration(Specification specification) throws InvalidSpecificationException {
        throw new InvalidSpecificationException(specification, "Not supported to parse specification!");
    }

}
