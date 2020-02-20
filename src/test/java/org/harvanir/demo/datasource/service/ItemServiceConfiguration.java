package org.harvanir.demo.datasource.service;

import org.harvanir.demo.datasource.configuration.AppConfiguration;
import org.harvanir.demo.datasource.configuration.RoutingDataSourceConfiguration;
import org.harvanir.demo.datasource.configuration.TransactionRoutingDataSourceConfiguration;
import org.harvanir.demo.datasource.jpa.model.Item;
import org.harvanir.demo.datasource.repository.ItemRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Harvan Irsyadi
 */
@EnableJpaRepositories(basePackageClasses = ItemRepository.class)
@EntityScan(basePackageClasses = Item.class)
@Import({RoutingDataSourceConfiguration.class, TransactionRoutingDataSourceConfiguration.class, AppConfiguration.class})
@TestConfiguration
public class ItemServiceConfiguration {

    @Bean
    public ItemService itemService(ItemRepository itemRepository, ServiceBeanMapper serviceBeanMapper) {
        return new ItemServiceImpl(itemRepository, serviceBeanMapper);
    }
}