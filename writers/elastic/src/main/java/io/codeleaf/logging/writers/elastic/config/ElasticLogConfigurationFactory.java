package io.codeleaf.logging.writers.elastic.config;

import io.codeleaf.config.impl.AbstractConfigurationFactory;
import io.codeleaf.config.spec.InvalidSettingException;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.SettingNotFoundException;
import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.util.Specifications;
import org.elasticsearch.common.transport.TransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public final class ElasticLogConfigurationFactory extends AbstractConfigurationFactory<ElasticLogConfiguration> {

    private static final int DEFAULT_PORT_NR = 9300;

    private static final ElasticLogConfiguration defaultConfiguration = new ElasticLogConfiguration(
            "docker-cluster",
            new TransportAddress[]{new TransportAddress(InetAddress.getLoopbackAddress(), DEFAULT_PORT_NR)},
            "codeleaf",
            "logs",
            true);

    public ElasticLogConfigurationFactory() {
        super(defaultConfiguration);
    }

    @Override
    protected ElasticLogConfiguration parseConfiguration(Specification specification) throws InvalidSpecificationException {
        return new ElasticLogConfiguration(
                specification.hasSetting("clusterName")
                        ? Specifications.parseString(specification, "clusterName")
                        : defaultConfiguration.getClusterName(),
                specification.hasSetting("clusterAddresses")
                        ? parseTransportAddresses(specification, "clusterAddresses")
                        : defaultConfiguration.getClusterAddresses(),
                specification.hasSetting("indexName")
                        ? Specifications.parseString(specification, "indexName")
                        : defaultConfiguration.getIndexName(),
                specification.hasSetting("typeBaseName")
                        ? Specifications.parseString(specification, "typeBaseName")
                        : defaultConfiguration.getTypeBaseName(),
                specification.hasSetting("ensureExists")
                        ? Specifications.parseBoolean(specification, "ensureExists")
                        : defaultConfiguration.isEnsureExists());
    }

    private TransportAddress[] parseTransportAddresses(Specification specification, String field) throws SettingNotFoundException, InvalidSettingException {
        try {
            String value = Specifications.parseString(specification, field);
            List<TransportAddress> transportAddresses = new ArrayList<>();
            for (String clusterAddress : value.split(",")) {
                transportAddresses.add(parseTransportAddress(clusterAddress));
            }
            return transportAddresses.toArray(new TransportAddress[0]);
        } catch (UnknownHostException | NumberFormatException cause) {
            throw new InvalidSettingException(specification, specification.getSetting(field), cause.getMessage(), cause);
        }
    }

    private TransportAddress parseTransportAddress(String value) throws UnknownHostException {
        String[] parts = value.trim().split(":");
        if (parts.length < 1 || parts.length > 2) {
            throw new IllegalArgumentException("Invalid address: " + value);
        }
        return new TransportAddress(
                InetAddress.getByName(parts[0]),
                parts.length == 2 ? Integer.parseInt(parts[1]) : DEFAULT_PORT_NR);
    }

}
