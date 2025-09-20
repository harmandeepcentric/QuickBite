package com.quickbite.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quickbite.dto.MenuItemCreateDTO;
import com.quickbite.dto.MenuItemResponseDTO;
import com.quickbite.dto.MenuItemUpdateDTO;
import com.quickbite.entity.MenuItem;
import com.quickbite.exception.DuplicateMenuItemNameException;
import com.quickbite.exception.MenuItemNotFoundException;
import com.quickbite.mapper.MenuItemMapper;
import com.quickbite.repository.MenuItemRepository;
import com.quickbite.service.MenuItemService;

/**
 * Implementation of MenuItemService interface.
 * 
 * This service class provides the business logic for managing menu items
 * including CRUD operations, validation, search, and filtering capabilities.
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private static final Logger logger = LoggerFactory.getLogger(MenuItemServiceImpl.class);

    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;
    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuItemMapper menuItemMapper,
            @Lazy MenuItemService menuItemService) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
        this.menuItemService = menuItemService;
    }

    @Override
    public MenuItemResponseDTO createMenuItem(MenuItemCreateDTO createDTO) {
        logger.info("Creating new menu item with name: {}", createDTO.getName());

        // Check if menu item with this name already exists
        if (menuItemRepository.existsByNameIgnoreCase(createDTO.getName())) {
            logger.warn("Attempt to create menu item with duplicate name: {}", createDTO.getName());
            throw DuplicateMenuItemNameException.forName(createDTO.getName());
        }

        // Convert DTO to entity and save
        MenuItem menuItem = menuItemMapper.toEntity(createDTO);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        logger.info("Successfully created menu item with ID: {}", savedMenuItem.getId());
        return menuItemMapper.toResponseDTO(savedMenuItem);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponseDTO getMenuItemById(Long id) {
        logger.debug("Fetching menu item with ID: {}", id);

        MenuItem menuItem = menuItemRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> {
                    logger.warn("Menu item not found with ID: {}", id);
                    return MenuItemNotFoundException.forId(id);
                });

        return menuItemMapper.toResponseDTO(menuItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getAllMenuItems() {
        logger.debug("Fetching all active menu items");

        List<MenuItem> menuItems = menuItemRepository.findByIsActiveTrue();
        return menuItems.stream()
                .map(menuItemMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemResponseDTO> getAllMenuItems(Pageable pageable) {
        logger.debug("Fetching all active menu items with pagination: {}", pageable);

        Page<MenuItem> menuItems = menuItemRepository.findByIsActiveTrue(pageable);
        return menuItems.map(menuItemMapper::toResponseDTO);
    }

    @Override
    public MenuItemResponseDTO updateMenuItem(Long id, MenuItemUpdateDTO updateDTO) {
        logger.info("Updating menu item with ID: {}", id);

        // Check if the update DTO has any changes
        if (!updateDTO.hasUpdates()) {
            logger.warn("No updates provided for menu item with ID: {}", id);
            throw new IllegalArgumentException("No update fields provided");
        }

        // Find the existing menu item
        MenuItem existingMenuItem = menuItemRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> {
                    logger.warn("Menu item not found for update with ID: {}", id);
                    return MenuItemNotFoundException.forId(id);
                });

        // Check for duplicate name if name is being updated
        if (updateDTO.getName() != null &&
                !updateDTO.getName().equalsIgnoreCase(existingMenuItem.getName()) &&
                menuItemRepository.existsByNameIgnoreCaseAndIdNot(updateDTO.getName(), id)) {
            logger.warn("Attempt to update menu item to duplicate name: {}", updateDTO.getName());
            throw DuplicateMenuItemNameException.forName(updateDTO.getName());
        }

        // Apply updates
        menuItemMapper.updateEntityFromDTO(existingMenuItem, updateDTO);
        MenuItem updatedMenuItem = menuItemRepository.save(existingMenuItem);

        logger.info("Successfully updated menu item with ID: {}", id);
        return menuItemMapper.toResponseDTO(updatedMenuItem);
    }

    @Override
    public void deleteMenuItem(Long id) {
        logger.info("Deleting menu item with ID: {}", id);

        MenuItem menuItem = menuItemRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> {
                    logger.warn("Menu item not found for deletion with ID: {}", id);
                    return MenuItemNotFoundException.forId(id);
                });

        // Soft delete - set isActive to false
        menuItem.setIsActive(false);
        menuItemRepository.save(menuItem);

        logger.info("Successfully deleted menu item with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> searchMenuItems(String searchTerm) {
        logger.debug("Searching menu items with term: {}", searchTerm);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return menuItemService.getAllMenuItems();
        }

        List<MenuItem> menuItems = menuItemRepository.searchByNameOrDescription(searchTerm.trim());
        return menuItems.stream()
                .map(menuItemMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemResponseDTO> searchMenuItems(String searchTerm, Pageable pageable) {
        logger.debug("Searching menu items with term: {} and pagination: {}", searchTerm, pageable);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return menuItemService.getAllMenuItems(pageable);
        }

        Page<MenuItem> menuItems = menuItemRepository.searchByNameOrDescription(searchTerm.trim(), pageable);
        return menuItems.map(menuItemMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getMenuItemsByCategory(String category) {
        logger.debug("Fetching menu items by category: {}", category);

        List<MenuItem> menuItems = menuItemRepository.findByCategoryIgnoreCase(category);
        return menuItems.stream()
                .map(menuItemMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemResponseDTO> getMenuItemsByCategory(String category, Pageable pageable) {
        logger.debug("Fetching menu items by category: {} with pagination: {}", category, pageable);

        Page<MenuItem> menuItems = menuItemRepository.findByCategoryIgnoreCase(category, pageable);
        return menuItems.map(menuItemMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getMenuItemsByDietaryTag(String dietaryTag) {
        logger.debug("Fetching menu items by dietary tag: {}", dietaryTag);

        List<MenuItem> menuItems = menuItemRepository.findByDietaryTagContainingIgnoreCase(dietaryTag);
        return menuItems.stream()
                .map(menuItemMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemResponseDTO> getMenuItemsByDietaryTag(String dietaryTag, Pageable pageable) {
        logger.debug("Fetching menu items by dietary tag: {} with pagination: {}", dietaryTag, pageable);

        Page<MenuItem> menuItems = menuItemRepository.findByDietaryTagContainingIgnoreCase(dietaryTag, pageable);
        return menuItems.map(menuItemMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getMenuItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.debug("Fetching menu items by price range: {} - {}", minPrice, maxPrice);

        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }

        List<MenuItem> menuItems = menuItemRepository.findByPriceBetween(minPrice, maxPrice);
        return menuItems.stream()
                .map(menuItemMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemResponseDTO> getMenuItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice,
            Pageable pageable) {
        logger.debug("Fetching menu items by price range: {} - {} with pagination: {}", minPrice, maxPrice, pageable);

        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }

        Page<MenuItem> menuItems = menuItemRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        return menuItems.map(menuItemMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        logger.debug("Fetching all distinct categories");
        return menuItemRepository.findDistinctCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllDietaryTags() {
        logger.debug("Fetching all distinct dietary tags");
        return menuItemRepository.findDistinctDietaryTags();
    }
}