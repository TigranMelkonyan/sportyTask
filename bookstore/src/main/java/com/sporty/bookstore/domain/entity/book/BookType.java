package com.sporty.bookstore.domain.entity.book;

import com.sporty.bookstore.domain.entity.common.audit.AuditableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Tigran Melkonyan
 * Date: 4/16/25
 * Time: 9:13 PM
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class BookType extends AuditableBaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private boolean eligibleForDiscount;

    @Column(nullable = false)
    private double priceMultiplier;
    
    @Column(nullable = false)
    private double bundleDiscount;

}
