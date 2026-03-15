package com.example.democoffeeshop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    private Long menuItemId;
    private String menuItemName;
    private int quantity;
    private BigDecimal unitPrice;
}

