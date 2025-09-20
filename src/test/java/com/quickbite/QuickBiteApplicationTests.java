package com.quickbite;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test for QuickBite Application startup and context loading.
 * 
 * This test ensures that the Spring Boot application context loads correctly
 * and all components are properly configured and wired.
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
class QuickBiteApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the application context loads successfully
        // If the context fails to load, this test will fail
    }
}