package com.quickbite.exception;

/**
 * Exception thrown when attempting to create a menu item with a name that
 * already exists.
 * 
 * This exception is used to enforce business rules around menu item name
 * uniqueness.
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
public class DuplicateMenuItemNameException extends RuntimeException {

    /**
     * Constructs a new DuplicateMenuItemNameException with the specified detail
     * message.
     * 
     * @param message the detail message
     */
    public DuplicateMenuItemNameException(String message) {
        super(message);
    }

    /**
     * Constructs a new DuplicateMenuItemNameException with the specified detail
     * message and cause.
     * 
     * @param message the detail message
     * @param cause   the cause
     */
    public DuplicateMenuItemNameException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new DuplicateMenuItemNameException for a specific menu item
     * name.
     * 
     * @param name the duplicate menu item name
     * @return a new DuplicateMenuItemNameException with a standard message
     */
    public static DuplicateMenuItemNameException forName(String name) {
        return new DuplicateMenuItemNameException("Menu item with name '" + name + "' already exists");
    }
}