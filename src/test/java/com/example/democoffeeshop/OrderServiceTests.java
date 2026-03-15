package com.example.democoffeeshop;

import com.example.democoffeeshop.dto.CreateOrderRequest;
import com.example.democoffeeshop.dto.OrderItemRequest;
import com.example.democoffeeshop.dto.OrderResponse;
import com.example.democoffeeshop.model.MenuItem;
import com.example.democoffeeshop.model.enums.Category;
import com.example.democoffeeshop.model.enums.OrderStatus;
import com.example.democoffeeshop.repository.MenuItemRepository;
import com.example.democoffeeshop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderServiceTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    void completesOrderWithDiscountWhenDrinkAndDessert() {
        MenuItem drink = new MenuItem();
        drink.setName("Latte");
        drink.setCategory(Category.DRINK);
        drink.setPrice(new BigDecimal("5.00"));

        MenuItem dessert = new MenuItem();
        dessert.setName("Cake");
        dessert.setCategory(Category.DESSERT);
        dessert.setPrice(new BigDecimal("4.00"));

        menuItemRepository.saveAll(List.of(drink, dessert));

        OrderItemRequest drinkItem = new OrderItemRequest();
        drinkItem.setMenuItemId(drink.getId());
        drinkItem.setQuantity(1);

        OrderItemRequest dessertItem = new OrderItemRequest();
        dessertItem.setMenuItemId(dessert.getId());
        dessertItem.setQuantity(1);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setItems(List.of(drinkItem, dessertItem));

        OrderResponse created = orderService.createOrder(request);
        assertThat(created.getStatus()).isEqualTo(OrderStatus.ACCEPTED);

        OrderResponse ready = orderService.updateStatus(created.getId(), OrderStatus.READY);
        assertThat(ready.getStatus()).isEqualTo(OrderStatus.READY);

        OrderResponse completed = orderService.updateStatus(created.getId(), OrderStatus.COMPLETED);
        assertThat(completed.getStatus()).isEqualTo(OrderStatus.COMPLETED);


        assertThat(completed.getFinalPrice()).isEqualByComparingTo("8.10");
    }
}

