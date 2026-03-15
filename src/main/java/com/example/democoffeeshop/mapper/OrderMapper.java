package com.example.democoffeeshop.mapper;

import com.example.democoffeeshop.dto.OrderItemResponse;
import com.example.democoffeeshop.dto.OrderResponse;
import com.example.democoffeeshop.model.CoffeeOrder;
import com.example.democoffeeshop.model.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponse(CoffeeOrder order);

    List<OrderResponse> toOrderResponses(List<CoffeeOrder> orders);

    OrderItemResponse toOrderItemResponse(OrderItem item);
}

