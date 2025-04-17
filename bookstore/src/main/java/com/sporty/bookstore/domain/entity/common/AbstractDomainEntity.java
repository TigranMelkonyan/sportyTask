package com.sporty.bookstore.domain.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/16/25
 * Time: 8:18 PM
 */
@Getter
@Setter
@MappedSuperclass
public class AbstractDomainEntity {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    @EqualsAndHashCode.Include
    protected UUID id;

}
