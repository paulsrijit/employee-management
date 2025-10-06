package com.example.employee;

import com.example.employee.EmployeeManagementApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class EmployeeManagementApplicationTest {

    @Test
    void contextLoads() {
        // This test verifies that the Spring Boot application context loads successfully
        // If there are any configuration issues, this test will fail
    }

    @Test
    void main() {
        // Test that main method doesn't throw any exceptions during startup
        // We're not actually starting the full application, just testing the method exists and is callable
        String[] args = {};
        
        // This would normally start the application, but in test context it just validates the setup
        // EmployeeManagementApplication.main(args);
        
        // Instead, we just verify the class exists and has the main method
        try {
            EmployeeManagementApplication.class.getDeclaredMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("Main method should exist", e);
        }
    }
}