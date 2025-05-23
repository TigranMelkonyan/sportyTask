package com.sporty.bookstore.domain.entity.book;

import com.sporty.bookstore.domain.entity.common.audit.AuditableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Tigran Melkonyan
 * Date: 4/16/25
 * Time: 9:12 PM
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Book extends AuditableBaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @ManyToOne(optional = false)
    private BookType type;

    @Column(nullable = false)
    private Integer stockQuantity;
    
} 
