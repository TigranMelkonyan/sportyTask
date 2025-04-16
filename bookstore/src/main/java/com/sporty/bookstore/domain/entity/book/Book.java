package com.sporty.bookstore.domain.entity.book;

import com.sporty.bookstore.domain.entity.common.audit.AuditableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Tigran Melkonyan
 * Date: 4/16/25
 * Time: 9:12 PM
 */
@Table(name = "books")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookType type;

    @Column(nullable = false)
    private Integer stockQuantity;
    
} 
