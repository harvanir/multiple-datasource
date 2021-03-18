package org.harvanir.demo.datasource.configuration;

import org.harvanir.demo.datasource.advice.RoutingDataSourceTransactionalAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Harvan Irsyadi
 */
@Configuration(proxyBeanMethods = false)
public class TransactionRoutingDataSourceAutoConfiguration {

    @Bean
    public RoutingDataSourceTransactionalAspect routingDataSourceTransactionalAspect() {
        return new RoutingDataSourceTransactionalAspect();
    }
}