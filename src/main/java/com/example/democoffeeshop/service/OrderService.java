package com.example.democoffeeshop.service;

import com.example.democoffeeshop.dto.BusinessStatisticsResponse;
import com.example.democoffeeshop.dto.CreateOrderRequest;
import com.example.democoffeeshop.dto.OrderItemRequest;
import com.example.democoffeeshop.dto.OrderResponse;
import com.example.democoffeeshop.dto.SoldItemDto;
import com.example.democoffeeshop.model.CoffeeOrder;
import com.example.democoffeeshop.model.enums.Category;
import com.example.democoffeeshop.model.MenuItem;
import com.example.democoffeeshop.model.OrderItem;
import com.example.democoffeeshop.model.enums.OrderStatus;
import com.example.democoffeeshop.mapper.OrderMapper;
import com.example.democoffeeshop.repository.CoffeeOrderRepository;
import com.example.democoffeeshop.repository.MenuItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final CoffeeOrderRepository coffeeOrderRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderMapper orderMapper;

    public OrderService(CoffeeOrderRepository coffeeOrderRepository,
                        MenuItemRepository menuItemRepository,
                        OrderMapper orderMapper) {
        this.coffeeOrderRepository = coffeeOrderRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        CoffeeOrder order = new CoffeeOrder();
        order.setStatus(OrderStatus.ACCEPTED);
        order.setCreatedAt(OffsetDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Menu item not found: " + itemRequest.getMenuItemId()
                    ));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItems.add(orderItem);

            BigDecimal lineTotal = menuItem.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(lineTotal);
        }

        order.setItems(orderItems);
        order.setFinalPrice(total);

        CoffeeOrder saved = coffeeOrderRepository.save(order);
        return orderMapper.toOrderResponse(saved);
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        CoffeeOrder order = coffeeOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order not found: " + id
                ));

        OrderStatus previousStatus = order.getStatus();
        order.setStatus(status);

        if (status == OrderStatus.COMPLETED && previousStatus != OrderStatus.COMPLETED) {
            boolean hasDrink = order.getItems().stream()
                    .anyMatch(i -> i.getMenuItem().getCategory() == Category.DRINK);
            boolean hasDessert = order.getItems().stream()
                    .anyMatch(i -> i.getMenuItem().getCategory() == Category.DESSERT);

            BigDecimal total = order.getItems().stream()
                    .map(i -> i.getMenuItem().getPrice()
                            .multiply(BigDecimal.valueOf(i.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (hasDrink && hasDessert) {
                total = total.multiply(BigDecimal.valueOf(0.9));
            }

            order.setFinalPrice(total);
            order.setCompletedAt(OffsetDateTime.now());
        }

        CoffeeOrder saved = coffeeOrderRepository.save(order);
        return orderMapper.toOrderResponse(saved);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        CoffeeOrder order = coffeeOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order not found: " + id
                ));
        return orderMapper.toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderMapper.toOrderResponses(coffeeOrderRepository.findAll());
    }

    @Transactional(readOnly = true)
    public BusinessStatisticsResponse getBusinessStatistics() {
        BigDecimal totalRevenue = coffeeOrderRepository.calculateTotalRevenue(OrderStatus.COMPLETED.name());

        List<SoldItemDto> soldItems = coffeeOrderRepository.findSoldItems(OrderStatus.COMPLETED.name())
                .stream()
                .map(p -> new SoldItemDto(p.getItemName(), p.getTotalQuantity()))
                .collect(Collectors.toList());

        return new BusinessStatisticsResponse(totalRevenue, soldItems);
    }

}

