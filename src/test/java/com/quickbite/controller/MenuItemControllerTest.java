package com.quickbite.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickbite.dto.MenuItemCreateDTO;
import com.quickbite.dto.MenuItemResponseDTO;
import com.quickbite.dto.MenuItemUpdateDTO;
import com.quickbite.exception.DuplicateMenuItemNameException;
import com.quickbite.exception.MenuItemNotFoundException;
import com.quickbite.service.MenuItemService;

/**
 * Unit tests for MenuItemController.
 * 
 * These tests follow TDD principles and verify the REST API endpoints
 * work correctly with proper HTTP status codes and response formats.
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
@WebMvcTest(MenuItemController.class)
@DisplayName("MenuItemController Tests")
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuItemService menuItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private MenuItemResponseDTO createSampleResponseDTO() {
        MenuItemResponseDTO responseDTO = new MenuItemResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Margherita Pizza");
        responseDTO.setDescription("Classic pizza with fresh mozzarella, tomato sauce, and basil");
        responseDTO.setPrice(new BigDecimal("12.99"));
        responseDTO.setCategory("Main Course");
        responseDTO.setDietaryTag("Vegetarian");
        responseDTO.setCreatedAt(LocalDateTime.now().minusDays(1));
        responseDTO.setUpdatedAt(LocalDateTime.now());
        return responseDTO;
    }

    @Test
    @DisplayName("Should create menu item successfully")
    void shouldCreateMenuItemSuccessfully() throws Exception {
        // Given
        MenuItemCreateDTO createDTO = new MenuItemCreateDTO(
                "Margherita Pizza",
                "Classic pizza with fresh mozzarella, tomato sauce, and basil",
                new BigDecimal("12.99"),
                "Main Course",
                "Vegetarian");

        MenuItemResponseDTO responseDTO = createSampleResponseDTO();

        when(menuItemService.createMenuItem(any(MenuItemCreateDTO.class)))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Margherita Pizza")))
                .andExpect(jsonPath("$.price", is(12.99)))
                .andExpect(jsonPath("$.category", is("Main Course")))
                .andExpect(jsonPath("$.dietaryTag", is("Vegetarian")));

        verify(menuItemService).createMenuItem(any(MenuItemCreateDTO.class));
    }

    @Test
    @DisplayName("Should return 400 when creating menu item with invalid data")
    void shouldReturn400WhenCreatingMenuItemWithInvalidData() throws Exception {
        // Given
        MenuItemCreateDTO invalidDTO = new MenuItemCreateDTO(
                "", // Invalid empty name
                "Description",
                new BigDecimal("-1.00"), // Invalid negative price
                "Category",
                "Tag");

        // When & Then
        mockMvc.perform(post("/api/v1/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(menuItemService, never()).createMenuItem(any());
    }

    @Test
    @DisplayName("Should return 409 when creating menu item with duplicate name")
    void shouldReturn409WhenCreatingMenuItemWithDuplicateName() throws Exception {
        // Given
        MenuItemCreateDTO createDTO = new MenuItemCreateDTO(
                "Existing Pizza",
                "Description",
                new BigDecimal("12.99"),
                "Main Course",
                "Vegetarian");

        when(menuItemService.createMenuItem(any(MenuItemCreateDTO.class)))
                .thenThrow(DuplicateMenuItemNameException.forName("Existing Pizza"));

        // When & Then
        mockMvc.perform(post("/api/v1/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("DUPLICATE_MENU_ITEM_NAME")));
    }

    @Test
    @DisplayName("Should get menu item by id successfully")
    void shouldGetMenuItemByIdSuccessfully() throws Exception {
        // Given
        Long menuItemId = 1L;
        MenuItemResponseDTO responseDTO = createSampleResponseDTO();

        when(menuItemService.getMenuItemById(menuItemId)).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(get("/api/v1/menu-items/{id}", menuItemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Margherita Pizza")));

        verify(menuItemService).getMenuItemById(menuItemId);
    }

    @Test
    @DisplayName("Should return 404 when menu item not found")
    void shouldReturn404WhenMenuItemNotFound() throws Exception {
        // Given
        Long nonExistentId = 999L;

        when(menuItemService.getMenuItemById(nonExistentId))
                .thenThrow(MenuItemNotFoundException.forId(nonExistentId));

        // When & Then
        mockMvc.perform(get("/api/v1/menu-items/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("MENU_ITEM_NOT_FOUND")));
    }

    @Test
    @DisplayName("Should get all menu items successfully")
    void shouldGetAllMenuItemsSuccessfully() throws Exception {
        // Given
        List<MenuItemResponseDTO> menuItems = Arrays.asList(
                createSampleResponseDTO(),
                createSampleResponseDTO());

        when(menuItemService.getAllMenuItems()).thenReturn(menuItems);

        // When & Then
        mockMvc.perform(get("/api/v1/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Margherita Pizza")));

        verify(menuItemService).getAllMenuItems();
    }

    @Test
    @DisplayName("Should get all menu items with pagination successfully")
    void shouldGetAllMenuItemsWithPaginationSuccessfully() throws Exception {
        // Given
        List<MenuItemResponseDTO> menuItems = Arrays.asList(createSampleResponseDTO());
        Page<MenuItemResponseDTO> page = new PageImpl<>(menuItems, PageRequest.of(0, 10), 1);

        when(menuItemService.getAllMenuItems(any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/v1/menu-items")
                .param("paginated", "true")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));

        verify(menuItemService).getAllMenuItems(any());
    }

    @Test
    @DisplayName("Should update menu item successfully")
    void shouldUpdateMenuItemSuccessfully() throws Exception {
        // Given
        Long menuItemId = 1L;
        MenuItemUpdateDTO updateDTO = new MenuItemUpdateDTO();
        updateDTO.setName("Updated Pizza");
        updateDTO.setPrice(new BigDecimal("15.99"));

        MenuItemResponseDTO responseDTO = createSampleResponseDTO();
        responseDTO.setName("Updated Pizza");
        responseDTO.setPrice(new BigDecimal("15.99"));

        when(menuItemService.updateMenuItem(anyLong(), any(MenuItemUpdateDTO.class)))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(put("/api/v1/menu-items/{id}", menuItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Pizza")))
                .andExpect(jsonPath("$.price", is(15.99)));

        verify(menuItemService).updateMenuItem(eq(menuItemId), any(MenuItemUpdateDTO.class));
    }

    @Test
    @DisplayName("Should delete menu item successfully")
    void shouldDeleteMenuItemSuccessfully() throws Exception {
        // Given
        Long menuItemId = 1L;

        doNothing().when(menuItemService).deleteMenuItem(menuItemId);

        // When & Then
        mockMvc.perform(delete("/api/v1/menu-items/{id}", menuItemId))
                .andExpect(status().isNoContent());

        verify(menuItemService).deleteMenuItem(menuItemId);
    }

    @Test
    @DisplayName("Should search menu items successfully")
    void shouldSearchMenuItemsSuccessfully() throws Exception {
        // Given
        String searchTerm = "pizza";
        List<MenuItemResponseDTO> searchResults = Arrays.asList(createSampleResponseDTO());

        when(menuItemService.searchMenuItems(searchTerm)).thenReturn(searchResults);

        // When & Then
        mockMvc.perform(get("/api/v1/menu-items/search")
                .param("q", searchTerm))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Margherita Pizza")));

        verify(menuItemService).searchMenuItems(searchTerm);
    }

    @Test
    @DisplayName("Should get menu items by category successfully")
    void shouldGetMenuItemsByCategorySuccessfully() throws Exception {
        // Given
        String category = "Main Course";
        List<MenuItemResponseDTO> categoryItems = Arrays.asList(createSampleResponseDTO());

        when(menuItemService.getMenuItemsByCategory(category)).thenReturn(categoryItems);

        // When & Then
        mockMvc.perform(get("/api/v1/menu-items/category/{category}", category))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].category", is("Main Course")));

        verify(menuItemService).getMenuItemsByCategory(category);
    }
}