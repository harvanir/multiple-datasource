package org.harvanir.demo.datasource.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.harvanir.demo.datasource.support.RoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * See org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration
 *
 * @author Harvan Irsyadi
 */
@Configuration
@EnableConfigurationProperties({DataSourceWrapperProperties.class, DataSourceRoutingProperties.class})
public class RoutingDataSourceConfiguration {

    private static final String DATA_SOURCE = "dataSource";

    private static final String DEFAULT_TARGET_DATA_SOURCE = "defaultTargetDataSource";

    private static final String PRIMARY_DATA_SOURCE_PROPERTIES = "primaryDataSourceProperties";

    @SuppressWarnings("unchecked")
    protected static <T> T createDataSource(DataSourceProperties properties) {
        return (T) properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = PRIMARY_DATA_SOURCE_PROPERTIES)
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = DEFAULT_TARGET_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariDataSource defaultTargetDataSource(@Qualifier(PRIMARY_DATA_SOURCE_PROPERTIES) DataSourceProperties primaryDataSourceProperties) {
        HikariDataSource dataSource = createDataSource(primaryDataSourceProperties);
        if (StringUtils.hasText(primaryDataSourceProperties.getName())) {
            dataSource.setPoolName(primaryDataSourceProperties.getName());
        }
        return dataSource;
    }

    private HikariDataSource createDecoratedDataSource(DataSourceWrapperProperties dataSourceWrapperProperties) {
        HikariDataSource dataSource = createDataSource(dataSourceWrapperProperties);
        DataSourceWrapperProperties.Hikari hikari = dataSourceWrapperProperties.getHikari();

        if (StringUtils.hasText(dataSourceWrapperProperties.getName())) {
            dataSource.setPoolName(dataSourceWrapperProperties.getName());
        }

        if (hikari.getConnectionTestQuery() != null) {
            dataSource.setConnectionTestQuery(hikari.getConnectionTestQuery());
        }

        dataSource.setConnectionTimeout(hikari.getConnectionTimeout());
        dataSource.setDriverClassName(hikari.getDriverClassName());
        dataSource.setIdleTimeout(hikari.getIdleTimeout());
        dataSource.setMaximumPoolSize(hikari.getMaximumPoolSize());
        dataSource.setMaxLifetime(hikari.getMaxLifetime());
        dataSource.setMinimumIdle(hikari.getMinimumIdle());
        dataSource.setValidationTimeout(hikari.getValidationTimeout());

        return dataSource;
    }

    private void populateTargetDataSource(AppProperties appProperties, Map<Object, Object> dataSourceMap) {
        for (DataSourceWrapperProperties dataSourceWrapperProperties : appProperties.getDatasources()) {
            HikariDataSource hikariDataSource = createDecoratedDataSource(dataSourceWrapperProperties);
            dataSourceMap.put(hikariDataSource.getPoolName(), hikariDataSource);
        }
    }

    @Primary
    @Bean(DATA_SOURCE)
    @DependsOn({DEFAULT_TARGET_DATA_SOURCE})
    public DataSource dataSource(@Qualifier(DEFAULT_TARGET_DATA_SOURCE) HikariDataSource defaultTargetDataSource
            , AppProperties appProperties, DataSourceRoutingProperties dataSourceRoutingProperties
    ) {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();

        dataSourceMap.put(defaultTargetDataSource.getPoolName(), defaultTargetDataSource);
        populateTargetDataSource(appProperties, dataSourceMap);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(defaultTargetDataSource);
        routingDataSource.setLenientFallback(dataSourceRoutingProperties.isLenientFallback());

        return routingDataSource;
    }
}