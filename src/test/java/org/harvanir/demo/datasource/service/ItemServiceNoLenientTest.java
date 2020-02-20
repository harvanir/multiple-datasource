package org.harvanir.demo.datasource.service;

import org.harvanir.demo.datasource.entity.ItemCreateRequest;
import org.harvanir.demo.datasource.entity.ItemResponse;
import org.harvanir.demo.datasource.repository.ItemRepository;
import org.harvanir.demo.datasource.support.DataSourceContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.CannotCreateTransactionException;

import java.math.BigDecimal;

/**
 * @author Harvan Irsyadi
 */
@ActiveProfiles("mysqlnoleinent")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@SpringBootTest(classes = ItemServiceConfiguration.class)
public class ItemServiceNoLenientTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    private String generatedId;

    @AfterEach
    void afterEach() {
        if (generatedId != null) {
            itemRepository.deleteById(generatedId);
        }

        DataSourceContextHolder.clear();
    }

    private void assertDataSource(String dataSourceKey) {
        DataSourceContextHolder.setRouteKey(dataSourceKey);

        ItemCreateRequest createRequest = new ItemCreateRequest();
        createRequest.setName("name");
        createRequest.setQuantity(10);
        createRequest.setPrice(BigDecimal.TEN);

        ItemResponse itemResponse = itemService.create(createRequest);
        if (itemResponse != null) {
            generatedId = itemResponse.getId();
        }
    }

    @Test
    void test1NullKeyDataToDs1DataSource() {
        Assertions.assertThrows(CannotCreateTransactionException.class, () ->
                assertDataSource(null)
        );
    }

    @Test
    void test2WrongKeyDataToDs1DataSource() {
        Assertions.assertThrows(CannotCreateTransactionException.class, () ->
                assertDataSource("unknownKey")
        );
    }
}