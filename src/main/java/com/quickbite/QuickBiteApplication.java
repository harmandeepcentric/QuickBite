package com.quickbite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * QuickBite Menu Management API
 * 
 * Main Spring Boot application class for the QuickBite restaurant
 * menu management system. This RESTful API provides comprehensive
 * CRUD operations for managing menu items.
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
@SpringBootApplication
public class QuickBiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickBiteApplication.class, args);
    }
}