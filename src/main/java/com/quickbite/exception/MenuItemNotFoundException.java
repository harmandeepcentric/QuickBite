package com.quickbite.exception;

/**
 * Exception thrown when a requested menu item is not found.
 * 
 * This exception is used throughout the application when a menu item
 * cannot be found by its ID or when the item exists but is inactive.
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
public class MenuItemNotFoundException extends RuntimeException {

    /**
     * Constructs a new MenuItemNotFoundException with the specified detail message.
     * 
     * @param message the detail message
     */
    public MenuItemNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new MenuItemNotFoundException with the specified detail message
     * and cause.
     * 
     * @param message the detail message
     * @param cause   the cause
     */
    public MenuItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new MenuItemNotFoundException for a specific menu item ID.
     * 
     * @param id the menu item ID that was not found
     * @return a new MenuItemNotFoundException with a standard message
     */
    public static MenuItemNotFoundException forId(Long id) {
        return new MenuItemNotFoundException("Menu item with ID " + id + " not found");
    }
}