package com.quickbite.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for updating an existing MenuItem.
 * 
 * This DTO is used for receiving menu item data from client requests
 * when updating existing menu items. All fields are optional to support
 * partial updates (PATCH operations).
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
public class MenuItemUpdateDTO {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price must not exceed 999,999.99")
    private BigDecimal price;

    @Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters")
    private String category;

    @Size(max = 100, message = "Dietary tag must not exceed 100 characters")
    private String dietaryTag;

    // Default constructor
    public MenuItemUpdateDTO() {
    }

    // Constructor with all fields
    public MenuItemUpdateDTO(String name, String description, BigDecimal price, String category, String dietaryTag) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.dietaryTag = dietaryTag;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDietaryTag() {
        return dietaryTag;
    }

    public void setDietaryTag(String dietaryTag) {
        this.dietaryTag = dietaryTag;
    }

    /**
     * Check if any field has been set (not null)
     * 
     * @return true if at least one field is not null
     */
    public boolean hasUpdates() {
        return name != null || description != null || price != null ||
                category != null || dietaryTag != null;
    }

    @Override
    public String toString() {
        return "MenuItemUpdateDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", dietaryTag='" + dietaryTag + '\'' +
                '}';
    }
}