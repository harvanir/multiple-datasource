package org.harvanir.demo.datasource.service;

import org.harvanir.demo.datasource.entity.ItemCreateRequest;
import org.harvanir.demo.datasource.entity.ItemResponse;
import org.harvanir.demo.datasource.jpa.model.Item;
import org.harvanir.demo.datasource.repository.ItemRepository;

import java.util.UUID;

/**
 * @author Harvan Irsyadi
 */
public class ItemServiceImpl  implements ItemService {

    private final ItemRepository itemRepository;

    private final ServiceBeanMapper serviceBeanMapper;

    public ItemServiceImpl(ItemRepository itemRepository, ServiceBeanMapper serviceBeanMapper) {
        this.itemRepository = itemRepository;
        this.serviceBeanMapper = serviceBeanMapper;
    }

    @Override
    public ItemResponse create(ItemCreateRequest createRequest) {
        Item item = serviceBeanMapper.toJpaModel(createRequest);
        item.setId(UUID.randomUUID().toString());

        return serviceBeanMapper.toResponseEntity(itemRepository.save(item));
    }
}