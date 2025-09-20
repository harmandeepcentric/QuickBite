package com.quickbite.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quickbite.dto.MenuItemCreateDTO;
import com.quickbite.dto.MenuItemResponseDTO;
import com.quickbite.dto.MenuItemUpdateDTO;
import com.quickbite.service.MenuItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;

/**
 * REST Controller for MenuItem operations.
 * 
 * This controller provides RESTful endpoints for managing menu items
 * including CRUD operations, search, and filtering capabilities.
 * All endpoints follow REST conventions and return appropriate HTTP status
 * codes.
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/menu-items")
@Tag(name = "Menu Items", description = "API for managing restaurant menu items")
public class MenuItemController {

    private static final Logger logger = LoggerFactory.getLogger(MenuItemController.class);

    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @Operation(summary = "Create a new menu item", description = "Creates a new menu item with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Menu item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Menu item name already exists")
    })
    @PostMapping
    public ResponseEntity<MenuItemResponseDTO> createMenuItem(
            @Valid @RequestBody MenuItemCreateDTO createDTO) {
        logger.info("POST /api/v1/menu-items - Creating menu item: {}", createDTO.getName());

        MenuItemResponseDTO createdMenuItem = menuItemService.createMenuItem(createDTO);
        return new ResponseEntity<>(createdMenuItem, HttpStatus.CREATED);
    }

    @Operation(summary = "Get menu item by ID", description = "Retrieves a menu item by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item found"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> getMenuItemById(
            @Parameter(description = "Menu item ID", required = true) @PathVariable @Positive Long id) {
        logger.debug("GET /api/v1/menu-items/{} - Fetching menu item", id);

        MenuItemResponseDTO menuItem = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }

    @Operation(summary = "Get all menu items", description = "Retrieves all active menu items with optional pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Object> getAllMenuItems(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Enable pagination") @RequestParam(defaultValue = "false") boolean paginated) {

        logger.debug("GET /api/v1/menu-items - Fetching menu items with pagination: {}", paginated);

        if (paginated) {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<MenuItemResponseDTO> menuItems = menuItemService.getAllMenuItems(pageable);
            return ResponseEntity.ok(menuItems);
        } else {
            List<MenuItemResponseDTO> menuItems = menuItemService.getAllMenuItems();
            return ResponseEntity.ok(menuItems);
        }
    }

    @Operation(summary = "Update menu item", description = "Updates an existing menu item with provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Menu item not found"),
            @ApiResponse(responseCode = "409", description = "Menu item name already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItem(
            @Parameter(description = "Menu item ID", required = true) @PathVariable @Positive Long id,
            @Valid @RequestBody MenuItemUpdateDTO updateDTO) {
        logger.info("PUT /api/v1/menu-items/{} - Updating menu item", id);

        MenuItemResponseDTO updatedMenuItem = menuItemService.updateMenuItem(id, updateDTO);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @Operation(summary = "Partially update menu item", description = "Partially updates an existing menu item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Menu item not found"),
            @ApiResponse(responseCode = "409", description = "Menu item name already exists")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> partialUpdateMenuItem(
            @Parameter(description = "Menu item ID", required = true) @PathVariable @Positive Long id,
            @Valid @RequestBody MenuItemUpdateDTO updateDTO) {
        logger.info("PATCH /api/v1/menu-items/{} - Partially updating menu item", id);

        MenuItemResponseDTO updatedMenuItem = menuItemService.updateMenuItem(id, updateDTO);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @Operation(summary = "Delete menu item", description = "Soft deletes a menu item by setting it as inactive")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menu item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(
            @Parameter(description = "Menu item ID", required = true) @PathVariable @Positive Long id) {
        logger.info("DELETE /api/v1/menu-items/{} - Deleting menu item", id);

        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search menu items", description = "Search menu items by name or description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<Object> searchMenuItems(
            @Parameter(description = "Search term", required = true) @RequestParam String q,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Enable pagination") @RequestParam(defaultValue = "false") boolean paginated) {

        logger.debug("GET /api/v1/menu-items/search - Searching with term: {}", q);

        if (paginated) {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<MenuItemResponseDTO> menuItems = menuItemService.searchMenuItems(q, pageable);
            return ResponseEntity.ok(menuItems);
        } else {
            List<MenuItemResponseDTO> menuItems = menuItemService.searchMenuItems(q);
            return ResponseEntity.ok(menuItems);
        }
    }

    @Operation(summary = "Get menu items by category", description = "Retrieves menu items filtered by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<Object> getMenuItemsByCategory(
            @Parameter(description = "Category name", required = true) @PathVariable String category,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Enable pagination") @RequestParam(defaultValue = "false") boolean paginated) {

        logger.debug("GET /api/v1/menu-items/category/{} - Fetching by category", category);

        if (paginated) {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<MenuItemResponseDTO> menuItems = menuItemService.getMenuItemsByCategory(category, pageable);
            return ResponseEntity.ok(menuItems);
        } else {
            List<MenuItemResponseDTO> menuItems = menuItemService.getMenuItemsByCategory(category);
            return ResponseEntity.ok(menuItems);
        }
    }

    @Operation(summary = "Get menu items by dietary tag", description = "Retrieves menu items filtered by dietary tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully")
    })
    @GetMapping("/dietary-tag/{dietaryTag}")
    public ResponseEntity<Object> getMenuItemsByDietaryTag(
            @Parameter(description = "Dietary tag", required = true) @PathVariable String dietaryTag,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Enable pagination") @RequestParam(defaultValue = "false") boolean paginated) {

        logger.debug("GET /api/v1/menu-items/dietary-tag/{} - Fetching by dietary tag", dietaryTag);

        if (paginated) {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<MenuItemResponseDTO> menuItems = menuItemService.getMenuItemsByDietaryTag(dietaryTag, pageable);
            return ResponseEntity.ok(menuItems);
        } else {
            List<MenuItemResponseDTO> menuItems = menuItemService.getMenuItemsByDietaryTag(dietaryTag);
            return ResponseEntity.ok(menuItems);
        }
    }

    @Operation(summary = "Get menu items by price range", description = "Retrieves menu items within a specified price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid price range")
    })
    @GetMapping("/price-range")
    public ResponseEntity<Object> getMenuItemsByPriceRange(
            @Parameter(description = "Minimum price", required = true) @RequestParam @DecimalMin("0.01") BigDecimal minPrice,
            @Parameter(description = "Maximum price", required = true) @RequestParam @DecimalMin("0.01") BigDecimal maxPrice,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "price") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Enable pagination") @RequestParam(defaultValue = "false") boolean paginated) {

        logger.debug("GET /api/v1/menu-items/price-range - Fetching by price range: {} - {}", minPrice, maxPrice);

        if (paginated) {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<MenuItemResponseDTO> menuItems = menuItemService.getMenuItemsByPriceRange(minPrice, maxPrice,
                    pageable);
            return ResponseEntity.ok(menuItems);
        } else {
            List<MenuItemResponseDTO> menuItems = menuItemService.getMenuItemsByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(menuItems);
        }
    }

    @Operation(summary = "Get all categories", description = "Retrieves all distinct menu item categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        logger.debug("GET /api/v1/menu-items/categories - Fetching all categories");

        List<String> categories = menuItemService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get all dietary tags", description = "Retrieves all distinct dietary tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dietary tags retrieved successfully")
    })
    @GetMapping("/dietary-tags")
    public ResponseEntity<List<String>> getAllDietaryTags() {
        logger.debug("GET /api/v1/menu-items/dietary-tags - Fetching all dietary tags");

        List<String> dietaryTags = menuItemService.getAllDietaryTags();
        return ResponseEntity.ok(dietaryTags);
    }
}