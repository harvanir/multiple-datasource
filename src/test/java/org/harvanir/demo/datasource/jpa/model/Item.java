package org.harvanir.demo.datasource.jpa.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Harvan Irsyadi
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "items")
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
public class Item {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "version")
    @Version
    private long version;
}
