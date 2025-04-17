package com.sporty.bookstore.domain.entity.order;

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
import java.util.UUID;

@Table(name = "customer_orders")
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class CustomerOrder extends AuditableBaseEntity {

    @Column(nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID customerId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private Integer totalItems;

    @Column(nullable = false)
    private Boolean loyaltyPointsApplied = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.PENDING;

} 