package org.harvanir.demo.datasource.configuration;

import org.harvanir.demo.datasource.advice.RoutingDataSourceAwareTransactionalAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Harvan Irsyadi
 */
@Configuration(proxyBeanMethods = false)
public class TransactionRoutingDataSourceConfiguration {

    @Bean
    public RoutingDataSourceAwareTransactionalAspect routingDataSourceAwareTransactionalAspect() {
        return new RoutingDataSourceAwareTransactionalAspect();
    }
}