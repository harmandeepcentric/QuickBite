package com.quickbite.mapper;

import org.springframework.stereotype.Component;

import com.quickbite.dto.MenuItemCreateDTO;
import com.quickbite.dto.MenuItemResponseDTO;
import com.quickbite.dto.MenuItemUpdateDTO;
import com.quickbite.entity.MenuItem;

/**
 * Mapper class for converting between MenuItem entity and DTOs.
 * 
 * This mapper handles the conversion between the MenuItem entity
 * and its corresponding Data Transfer Objects (DTOs) used in
 * API requests and responses.
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
@Component
public class MenuItemMapper {

    /**
     * Convert MenuItemCreateDTO to MenuItem entity
     * 
     * @param createDTO the create DTO
     * @return the MenuItem entity
     */
    public MenuItem toEntity(MenuItemCreateDTO createDTO) {
        if (createDTO == null) {
            return null;
        }

        MenuItem menuItem = new MenuItem();
        menuItem.setName(createDTO.getName());
        menuItem.setDescription(createDTO.getDescription());
        menuItem.setPrice(createDTO.getPrice());
        menuItem.setCategory(createDTO.getCategory());
        menuItem.setDietaryTag(createDTO.getDietaryTag());

        return menuItem;
    }

    /**
     * Convert MenuItem entity to MenuItemResponseDTO
     * 
     * @param entity the MenuItem entity
     * @return the response DTO
     */
    public MenuItemResponseDTO toResponseDTO(MenuItem entity) {
        if (entity == null) {
            return null;
        }

        MenuItemResponseDTO responseDTO = new MenuItemResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setName(entity.getName());
        responseDTO.setDescription(entity.getDescription());
        responseDTO.setPrice(entity.getPrice());
        responseDTO.setCategory(entity.getCategory());
        responseDTO.setDietaryTag(entity.getDietaryTag());
        responseDTO.setCreatedAt(entity.getCreatedAt());
        responseDTO.setUpdatedAt(entity.getUpdatedAt());

        return responseDTO;
    }

    /**
     * Update MenuItem entity with data from MenuItemUpdateDTO
     * Only updates non-null fields from the DTO
     * 
     * @param entity    the MenuItem entity to update
     * @param updateDTO the update DTO
     */
    public void updateEntityFromDTO(MenuItem entity, MenuItemUpdateDTO updateDTO) {
        if (entity == null || updateDTO == null) {
            return;
        }

        if (updateDTO.getName() != null) {
            entity.setName(updateDTO.getName());
        }

        if (updateDTO.getDescription() != null) {
            entity.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getPrice() != null) {
            entity.setPrice(updateDTO.getPrice());
        }

        if (updateDTO.getCategory() != null) {
            entity.setCategory(updateDTO.getCategory());
        }

        if (updateDTO.getDietaryTag() != null) {
            entity.setDietaryTag(updateDTO.getDietaryTag());
        }
    }
}