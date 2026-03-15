package com.example.democoffeeshop.dto;

import com.example.democoffeeshop.model.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;
    private OrderStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime completedAt;
    private BigDecimal finalPrice;
    private List<OrderItemResponse> items;
}

