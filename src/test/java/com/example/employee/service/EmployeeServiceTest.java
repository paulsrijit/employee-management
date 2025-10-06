package com.example.employee.service;

import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setSalary(75000.0);
        employee.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void create_ShouldReturnSavedEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.create(employee);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        verify(employeeRepository).save(employee);
    }

    @Test
    void listAll_ShouldReturnAllEmployees() {
        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Jane Smith");
        employee2.setEmail("jane.smith@example.com");

        List<Employee> expectedEmployees = Arrays.asList(employee, employee2);
        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        List<Employee> result = employeeService.listAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(employee, employee2);
        verify(employeeRepository).findAll();
    }

    @Test
    void findById_ShouldReturnEmployee_WhenEmployeeExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        verify(employeeRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenEmployeeNotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.findById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee not found");
        verify(employeeRepository).findById(999L);
    }

    @Test
    void update_ShouldReturnUpdatedEmployee_WhenEmployeeExists() {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("John Updated");
        updatedEmployee.setEmail("john.updated@example.com");
        updatedEmployee.setDepartment("HR");
        updatedEmployee.setSalary(80000.0);

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setName("John Updated");
        savedEmployee.setEmail("john.updated@example.com");
        savedEmployee.setDepartment("HR");
        savedEmployee.setSalary(80000.0);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        Employee result = employeeService.update(1L, updatedEmployee);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Updated");
        assertThat(result.getEmail()).isEqualTo("john.updated@example.com");
        assertThat(result.getDepartment()).isEqualTo("HR");
        assertThat(result.getSalary()).isEqualTo(80000.0);
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void update_ShouldThrowException_WhenEmployeeNotFound() {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("John Updated");

        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.update(999L, updatedEmployee))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee not found");
        verify(employeeRepository).findById(999L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.delete(1L);

        verify(employeeRepository).deleteById(1L);
    }
}