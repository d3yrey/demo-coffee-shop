package com.example.democoffeeshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class BusinessStatisticsResponse {

    private BigDecimal totalRevenue;
    private List<SoldItemDto> soldItems;
}

