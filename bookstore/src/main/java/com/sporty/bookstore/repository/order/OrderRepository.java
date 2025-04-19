package com.sporty.bookstore.repository.order;

import com.sporty.bookstore.domain.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 10:19 PM
 */
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o WHERE o.customerId =:customerId and o.status = 'ACTIVE'")
    Page<Order> findOrdersByCustomer(Pageable pageable, @Param("customerId") UUID customerId);
    
    @Query("SELECT o FROM Order o WHERE o.status = 'ACTIVE'")
    Page<Order> findOrders(Pageable pageable);

} 