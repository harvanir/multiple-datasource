package org.harvanir.demo.datasource.repository;

import org.harvanir.demo.datasource.jpa.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Harvan Irsyadi
 */
public interface ItemRepository extends JpaRepository<Item, String> {
}
