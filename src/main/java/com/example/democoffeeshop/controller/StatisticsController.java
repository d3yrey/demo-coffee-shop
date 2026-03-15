package com.example.democoffeeshop.controller;

import com.example.democoffeeshop.dto.BusinessStatisticsResponse;
import com.example.democoffeeshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final OrderService orderService;

    @GetMapping("/revenue")
    public BusinessStatisticsResponse getBusinessStatistics() {
        return orderService.getBusinessStatistics();
    }
}

