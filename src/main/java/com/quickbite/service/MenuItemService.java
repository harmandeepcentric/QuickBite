package com.quickbite.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.quickbite.dto.MenuItemCreateDTO;
import com.quickbite.dto.MenuItemResponseDTO;
import com.quickbite.dto.MenuItemUpdateDTO;

/**
 * Service interface for MenuItem operations.
 * 
 * This interface defines the business logic operations for managing menu items
 * including CRUD operations, search, and filtering capabilities.
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
public interface MenuItemService {

    /**
     * Create a new menu item
     * 
     * @param createDTO the menu item data
     * @return the created menu item
     * @throws IllegalArgumentException if the menu item name already exists
     */
    MenuItemResponseDTO createMenuItem(MenuItemCreateDTO createDTO);

    /**
     * Get a menu item by ID
     * 
     * @param id the menu item ID
     * @return the menu item if found
     * @throws RuntimeException if the menu item is not found
     */
    MenuItemResponseDTO getMenuItemById(Long id);

    /**
     * Get all active menu items
     * 
     * @return list of all active menu items
     */
    List<MenuItemResponseDTO> getAllMenuItems();

    /**
     * Get all active menu items with pagination
     * 
     * @param pageable pagination information
     * @return page of menu items
     */
    Page<MenuItemResponseDTO> getAllMenuItems(Pageable pageable);

    /**
     * Update a menu item
     * 
     * @param id        the menu item ID
     * @param updateDTO the update data
     * @return the updated menu item
     * @throws RuntimeException         if the menu item is not found
     * @throws IllegalArgumentException if the new name already exists
     */
    MenuItemResponseDTO updateMenuItem(Long id, MenuItemUpdateDTO updateDTO);

    /**
     * Delete a menu item (soft delete)
     * 
     * @param id the menu item ID
     * @throws RuntimeException if the menu item is not found
     */
    void deleteMenuItem(Long id);

    /**
     * Search menu items by name or description
     * 
     * @param searchTerm the search term
     * @return list of matching menu items
     */
    List<MenuItemResponseDTO> searchMenuItems(String searchTerm);

    /**
     * Search menu items by name or description with pagination
     * 
     * @param searchTerm the search term
     * @param pageable   pagination information
     * @return page of matching menu items
     */
    Page<MenuItemResponseDTO> searchMenuItems(String searchTerm, Pageable pageable);

    /**
     * Get menu items by category
     * 
     * @param category the category
     * @return list of menu items in the category
     */
    List<MenuItemResponseDTO> getMenuItemsByCategory(String category);

    /**
     * Get menu items by category with pagination
     * 
     * @param category the category
     * @param pageable pagination information
     * @return page of menu items in the category
     */
    Page<MenuItemResponseDTO> getMenuItemsByCategory(String category, Pageable pageable);

    /**
     * Get menu items by dietary tag
     * 
     * @param dietaryTag the dietary tag
     * @return list of menu items with the dietary tag
     */
    List<MenuItemResponseDTO> getMenuItemsByDietaryTag(String dietaryTag);

    /**
     * Get menu items by dietary tag with pagination
     * 
     * @param dietaryTag the dietary tag
     * @param pageable   pagination information
     * @return page of menu items with the dietary tag
     */
    Page<MenuItemResponseDTO> getMenuItemsByDietaryTag(String dietaryTag, Pageable pageable);

    /**
     * Get menu items within a price range
     * 
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of menu items within the price range
     */
    List<MenuItemResponseDTO> getMenuItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Get menu items within a price range with pagination
     * 
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @param pageable pagination information
     * @return page of menu items within the price range
     */
    Page<MenuItemResponseDTO> getMenuItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Get all distinct categories
     * 
     * @return list of all categories
     */
    List<String> getAllCategories();

    /**
     * Get all distinct dietary tags
     * 
     * @return list of all dietary tags
     */
    List<String> getAllDietaryTags();
}