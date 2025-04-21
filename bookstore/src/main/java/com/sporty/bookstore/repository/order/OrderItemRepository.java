package com.sporty.bookstore.repository.order;

import com.sporty.bookstore.domain.entity.order.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 10:21 PM
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    @Query("select o from OrderItem o where o.order.id =:orderId and o.status = 'ACTIVE'")
    Page<OrderItem> findAllForOrderId(@Param("orderId") UUID orderId, Pageable pageable);    
} 