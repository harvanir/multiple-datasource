package org.harvanir.demo.datasource.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Harvan Irsyadi
 */
@Getter
@Setter
@ToString
public class ItemUpdateRequest extends ItemCreateRequest {

    private String id;
}
