package com.example.democoffeeshop.repository;

import com.example.democoffeeshop.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {

    @Query(value = """
            SELECT COALESCE(SUM(o.final_price), 0)
            FROM coffee_orders o
            WHERE o.status = :status
            """, nativeQuery = true)
    BigDecimal calculateTotalRevenue(@Param("status") String status);

    interface SoldItemProjection {
        String getItemName();

        Long getTotalQuantity();
    }

    @Query(value = """
            SELECT mi.name AS itemName, SUM(oi.quantity) AS totalQuantity
            FROM coffee_orders o
            JOIN order_items oi ON o.id = oi.order_id
            JOIN menu_items mi ON mi.id = oi.menu_item_id
            WHERE o.status = :status
            GROUP BY mi.name
            ORDER BY mi.name
            """, nativeQuery = true)
    List<SoldItemProjection> findSoldItems(@Param("status") String status);
}

