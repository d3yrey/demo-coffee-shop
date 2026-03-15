package com.example.democoffeeshop.dto;

import com.example.democoffeeshop.model.enums.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemDto {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Category category;

    @NotNull
    @Min(0)
    private BigDecimal price;
}

