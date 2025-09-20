package com.quickbite.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenAPI/Swagger configuration for the QuickBite Menu Management API.
 * 
 * This configuration class sets up comprehensive API documentation including
 * API information, contact details, licensing, and server information.
 * The generated documentation will be accessible at /swagger-ui.html
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server"),
                        new Server()
                                .url("https://api.quickbite.com")
                                .description("Production Server")));
    }

    private Info apiInfo() {
        return new Info()
                .title("QuickBite Menu Management API")
                .description("RESTful API for managing restaurant menu items with comprehensive CRUD operations, " +
                        "search capabilities, and filtering options. Built with Spring Boot and SQLite following " +
                        "Test-Driven Development (TDD) practices.")
                .version("1.0.0")
                .contact(apiContact())
                .license(apiLicense());
    }

    private Contact apiContact() {
        return new Contact()
                .name("QuickBite Development Team")
                .email("api-support@quickbite.com")
                .url("https://www.quickbite.com/contact");
    }

    private License apiLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }
}