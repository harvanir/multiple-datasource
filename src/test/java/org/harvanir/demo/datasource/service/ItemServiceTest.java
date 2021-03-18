package org.harvanir.demo.datasource.service;

import org.harvanir.demo.datasource.entity.ItemCreateRequest;
import org.harvanir.demo.datasource.entity.ItemResponse;
import org.harvanir.demo.datasource.jpa.model.Item;
import org.harvanir.demo.datasource.repository.ItemRepository;
import org.harvanir.demo.datasource.support.DataSourceContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Harvan Irsyadi
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("mysql")
@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest(classes = ItemServiceConfiguration.class)
class ItemServiceTest {

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
        assertDataSource(dataSourceKey, null);
    }

    private void assertDataSource(String dataSourceKey, String validateNullKeyWithThisKey) {
        DataSourceContextHolder.setRouteKey(dataSourceKey);

        ItemCreateRequest createRequest = new ItemCreateRequest();
        createRequest.setName("name");
        createRequest.setQuantity(10);
        createRequest.setPrice(BigDecimal.TEN);

        ItemResponse itemResponse = itemService.create(createRequest);
        DataSourceContextHolder.setRouteKey(dataSourceKey != null ? dataSourceKey : validateNullKeyWithThisKey);
        Optional<Item> byId = itemRepository.findById(itemResponse.getId());
        assertTrue(byId.isPresent());

        Item item = byId.get();

        assertNotNull(item);
        generatedId = item.getId();
        assertEquals(itemResponse.getId(), item.getId());
        assertEquals(itemResponse.getName(), item.getName());
    }

    @Test
    void test1DataToPrimaryDataSource() {
        assertDataSource("ds_1");
    }

    @Test
    void test2DataToDs2DataSource() {
        assertDataSource("ds_2");
    }

    @Test
    void test3DataToDs3DataSource() {
        assertDataSource("ds_3");
    }

    @Test
    void test4NullKeyDataToDs1DataSource() {
        assertDataSource(null, "ds_1");
    }

    @Test
    void test5WrongKeyDataToDs1DataSource() {
        assertDataSource("unknownKey");
    }
}