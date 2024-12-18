package com.sevar.ecommerce.repository;

import com.sevar.ecommerce.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Long>
{
    @Query("SELECT o FROM Orders o WHERE o.user.id = :userId AND" +
            " o.orderStatus IN ('PENDING','PLACED', 'CONFIRMED', 'SHIPPED', 'DELIVERED','CANCELLED')")
    List<Orders> getUsersOrder(@Param("userId") Long userId);
}
