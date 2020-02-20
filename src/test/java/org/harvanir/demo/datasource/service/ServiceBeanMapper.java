package org.harvanir.demo.datasource.service;

import org.harvanir.demo.datasource.entity.ItemCreateRequest;
import org.harvanir.demo.datasource.entity.ItemResponse;
import org.harvanir.demo.datasource.jpa.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Harvan Irsyadi
 */
@Mapper(componentModel = "spring")
public interface ServiceBeanMapper {

    @Mapping(target = "id", ignore = true)
    Item toJpaModel(ItemCreateRequest itemCreateRequest);

    ItemResponse toResponseEntity(Item item);
}
