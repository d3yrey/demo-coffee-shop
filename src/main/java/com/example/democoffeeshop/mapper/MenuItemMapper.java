package com.example.democoffeeshop.mapper;

import com.example.democoffeeshop.dto.MenuItemDto;
import com.example.democoffeeshop.model.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    MenuItemDto toDto(MenuItem entity);

    List<MenuItemDto> toDtos(List<MenuItem> entities);

    MenuItem toEntity(MenuItemDto dto);

    void updateEntityFromDto(MenuItemDto dto, @MappingTarget MenuItem entity);
}

