package org.harvanir.demo.datasource.service;

import org.harvanir.demo.datasource.entity.ItemCreateRequest;
import org.harvanir.demo.datasource.entity.ItemResponse;

/**
 * @author Harvan Irsyadi
 */
public interface ItemService {

    ItemResponse create(ItemCreateRequest createRequest);
}
