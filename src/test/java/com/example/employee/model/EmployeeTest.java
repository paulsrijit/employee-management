package com.example.employee.model;

import com.example.employee.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
    }

    @Test
    void testEmployeeCreation() {
        assertThat(employee).isNotNull();
        assertThat(employee.getId()).isNull();
        assertThat(employee.getName()).isNull();
        assertThat(employee.getEmail()).isNull();
        assertThat(employee.getDepartment()).isNull();
        assertThat(employee.getSalary()).isNull();
        assertThat(employee.getCreatedAt()).isNotNull(); // Should be set by default
    }

    @Test
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setSalary(75000.0);
        employee.setCreatedAt(now);

        assertThat(employee.getId()).isEqualTo(1L);
        assertThat(employee.getName()).isEqualTo("John Doe");
        assertThat(employee.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(employee.getDepartment()).isEqualTo("IT");
        assertThat(employee.getSalary()).isEqualTo(75000.0);
        assertThat(employee.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testDefaultCreatedAtIsSet() {
        Employee newEmployee = new Employee();
        assertThat(newEmployee.getCreatedAt()).isNotNull();
        assertThat(newEmployee.getCreatedAt()).isBefore(LocalDateTime.now().plusSeconds(1));
    }

    @Test
    void testEmployeeWithAllFields() {
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 10, 0, 0);
        
        employee.setId(100L);
        employee.setName("Jane Smith");
        employee.setEmail("jane.smith@company.com");
        employee.setDepartment("Human Resources");
        employee.setSalary(65000.0);
        employee.setCreatedAt(createdAt);

        assertThat(employee.getId()).isEqualTo(100L);
        assertThat(employee.getName()).isEqualTo("Jane Smith");
        assertThat(employee.getEmail()).isEqualTo("jane.smith@company.com");
        assertThat(employee.getDepartment()).isEqualTo("Human Resources");
        assertThat(employee.getSalary()).isEqualTo(65000.0);
        assertThat(employee.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void testEmployeeWithNullValues() {
        employee.setId(null);
        employee.setName(null);
        employee.setEmail(null);
        employee.setDepartment(null);
        employee.setSalary(null);
        employee.setCreatedAt(null);

        assertThat(employee.getId()).isNull();
        assertThat(employee.getName()).isNull();
        assertThat(employee.getEmail()).isNull();
        assertThat(employee.getDepartment()).isNull();
        assertThat(employee.getSalary()).isNull();
        assertThat(employee.getCreatedAt()).isNull();
    }

    @Test
    void testSalaryWithDifferentValues() {
        employee.setSalary(0.0);
        assertThat(employee.getSalary()).isEqualTo(0.0);

        employee.setSalary(999999.99);
        assertThat(employee.getSalary()).isEqualTo(999999.99);

        employee.setSalary(-1000.0);
        assertThat(employee.getSalary()).isEqualTo(-1000.0);
    }
}