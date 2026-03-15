package com.example.democoffeeshop.controller;

import com.example.democoffeeshop.dto.MenuItemDto;
import com.example.democoffeeshop.mapper.MenuItemMapper;
import com.example.democoffeeshop.model.MenuItem;
import com.example.democoffeeshop.repository.MenuItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    @GetMapping
    public List<MenuItemDto> getAll() {
        return menuItemMapper.toDtos(menuItemRepository.findAll());
    }

    @GetMapping("/{id}")
    public MenuItemDto getById(@PathVariable Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found: " + id));
        return menuItemMapper.toDto(item);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemDto create(@Valid @RequestBody MenuItemDto dto) {
        MenuItem entity = menuItemMapper.toEntity(dto);
        return menuItemMapper.toDto(menuItemRepository.save(entity));
    }

    @PutMapping("/{id}")
    public MenuItemDto update(@PathVariable Long id, @Valid @RequestBody MenuItemDto dto) {
        MenuItem entity = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found: " + id));

        menuItemMapper.updateEntityFromDto(dto, entity);

        return menuItemMapper.toDto(menuItemRepository.save(entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu item not found: " + id);
        }
        menuItemRepository.deleteById(id);
    }

}

