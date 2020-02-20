package org.harvanir.demo.datasource.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Harvan Irsyadi
 */
@Getter
@Setter
@ToString
public class ItemCreateRequest {

    private String name;

    private Integer quantity;

    private BigDecimal price;

    private Date createdAt;

    private Date updatedAt;

    private long version;
}
