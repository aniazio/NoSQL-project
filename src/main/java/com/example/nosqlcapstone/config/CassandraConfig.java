package com.example.nosqlcapstone.config;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import java.util.List;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.*;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@EnableCassandraRepositories
@Configuration
@ConfigurationProperties(prefix = "spring.cassandra")
@Setter
public class CassandraConfig extends AbstractCassandraConfiguration {

    private String contactPoints;
    private String keyspaceName;
    private String basePackages;

    @Override
    protected String getKeyspaceName() {
        return keyspaceName;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }
    @Override
    public String[] getEntityBasePackages() {
        return new String[] {basePackages};
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        final CreateKeyspaceSpecification specification =
                CreateKeyspaceSpecification.createKeyspace(keyspaceName)
                        .ifNotExists()
                        .withSimpleReplication(3);
        return List.of(specification);
    }

    @Override
    protected DriverConfigLoaderBuilderConfigurer getDriverConfigLoaderBuilderConfigurer() {
        return driverLoader -> driverLoader.withString(DefaultDriverOption.REQUEST_CONSISTENCY, "QUORUM");
    }

}