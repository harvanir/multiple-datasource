package org.harvanir.demo.datasource.configuration;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
import io.micrometer.core.instrument.MeterRegistry;
import org.harvanir.demo.datasource.support.RoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Harvan Irsyadi
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(name = {
        "org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration",
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration"
})
@ConditionalOnClass({RoutingDataSource.class, MeterRegistry.class})
@ConditionalOnBean({RoutingDataSource.class, MeterRegistry.class})
public class DataSourceMetricsAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(HikariDataSource.class)
    static class HikariDataSourceMetricsConfiguration {

        private static final Logger logger = LoggerFactory.getLogger(HikariDataSourceMetricsConfiguration.class);

        HikariDataSourceMetricsConfiguration(ApplicationContext applicationContext) {
            logger.info("Configuring Hikari datasource metrics...");

            MeterRegistry registry = applicationContext.getBean(MeterRegistry.class);
            RoutingDataSource routingDataSource = applicationContext.getBean(RoutingDataSource.class);

            bindMetricsRegistryToHikariDataSources(registry, routingDataSource);

            logger.info("Hikari datasource metrics configured...");
        }

        private void bindMetricsRegistryToHikariDataSources(MeterRegistry registry, RoutingDataSource routingDataSource) {
            for (DataSource dataSource : routingDataSource.getResolvedDataSources().values()) {
                HikariDataSource hikariDataSource = DataSourceUnwrapper.unwrap(dataSource, HikariDataSource.class);
                if (hikariDataSource != null) {
                    bindMetricsRegistryToHikariDataSource(registry, hikariDataSource);
                }
            }
        }

        private void bindMetricsRegistryToHikariDataSource(MeterRegistry registry, HikariDataSource hikari) {
            if (hikari.getMetricRegistry() == null && hikari.getMetricsTrackerFactory() == null) {
                try {
                    hikari.setMetricsTrackerFactory(new MicrometerMetricsTrackerFactory(registry));
                } catch (Exception ex) {
                    logger.warn(String.format("Failed to bind Hikari metrics: %s", ex.getMessage()));
                }
            }
        }
    }
}