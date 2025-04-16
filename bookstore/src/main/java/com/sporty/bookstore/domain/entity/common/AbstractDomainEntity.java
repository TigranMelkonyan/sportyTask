package com.sporty.bookstore.domain.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/16/25
 * Time: 8:18 PM
 */
@MappedSuperclass
@Getter
@Setter
public class AbstractDomainEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "UUID")
    @GeneratedValue
    @EqualsAndHashCode.Include
    protected UUID id;
    
}
