package com.example.employee.repository;

import com.example.employee.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void findById_ShouldReturnEmployee_WhenEmployeeExists() {
        // Given
        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setSalary(75000.0);
        employee.setCreatedAt(LocalDateTime.now());

        Employee savedEmployee = entityManager.persistAndFlush(employee);

        // When
        Optional<Employee> found = employeeRepository.findById(savedEmployee.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("John Doe");
        assertThat(found.get().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(found.get().getDepartment()).isEqualTo("IT");
        assertThat(found.get().getSalary()).isEqualTo(75000.0);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenEmployeeDoesNotExist() {
        // When
        Optional<Employee> found = employeeRepository.findById(999L);

        // Then
        assertThat(found).isNotPresent();
    }

    @Test
    void findAll_ShouldReturnAllEmployees() {
        // Given
        Employee employee1 = new Employee();
        employee1.setName("John Doe");
        employee1.setEmail("john.doe@example.com");
        employee1.setDepartment("IT");
        employee1.setSalary(75000.0);

        Employee employee2 = new Employee();
        employee2.setName("Jane Smith");
        employee2.setEmail("jane.smith@example.com");
        employee2.setDepartment("HR");
        employee2.setSalary(65000.0);

        entityManager.persist(employee1);
        entityManager.persist(employee2);
        entityManager.flush();

        // When
        List<Employee> employees = employeeRepository.findAll();

        // Then
        assertThat(employees).hasSize(2);
        assertThat(employees).extracting(Employee::getName)
                .containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @Test
    void save_ShouldPersistEmployee() {
        // Given
        Employee employee = new Employee();
        employee.setName("Alice Johnson");
        employee.setEmail("alice.johnson@example.com");
        employee.setDepartment("Finance");
        employee.setSalary(70000.0);

        // When
        Employee saved = employeeRepository.save(employee);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Alice Johnson");
        assertThat(saved.getEmail()).isEqualTo("alice.johnson@example.com");
        assertThat(saved.getDepartment()).isEqualTo("Finance");
        assertThat(saved.getSalary()).isEqualTo(70000.0);

        // Verify it's actually persisted
        Employee found = entityManager.find(Employee.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Alice Johnson");
    }

    @Test
    void save_ShouldUpdateExistingEmployee() {
        // Given
        Employee employee = new Employee();
        employee.setName("Bob Wilson");
        employee.setEmail("bob.wilson@example.com");
        employee.setDepartment("IT");
        employee.setSalary(80000.0);

        Employee saved = entityManager.persistAndFlush(employee);
        entityManager.clear();
        
        Employee foundBeforeUpdate = entityManager.find(Employee.class, saved.getId());
        
        assertThat(foundBeforeUpdate.getId()).isEqualTo(saved.getId());
        assertThat(foundBeforeUpdate.getName()).isEqualTo("Bob Wilson");
        assertThat(foundBeforeUpdate.getSalary()).isEqualTo(80000.0);

        // Save through Repository
        foundBeforeUpdate.setName("Bob Wilson Jr.");
        foundBeforeUpdate.setSalary(85000.0);
        employeeRepository.save(foundBeforeUpdate);
        
		// Force DB sync
		entityManager.flush();
		entityManager.clear();
		
		Employee foundAfterUpdate = entityManager.find(Employee.class, saved.getId());

        // Then
        assertThat(foundAfterUpdate.getId()).isEqualTo(saved.getId());
        assertThat(foundAfterUpdate.getName()).isEqualTo("Bob Wilson Jr.");
        assertThat(foundAfterUpdate.getSalary()).isEqualTo(85000.0);
    }

    @Test
    void delete_ShouldRemoveEmployee() {
        // Given
        Employee employee = new Employee();
        employee.setName("Charlie Brown");
        employee.setEmail("charlie.brown@example.com");
        employee.setDepartment("Marketing");
        employee.setSalary(60000.0);

        Employee saved = entityManager.persistAndFlush(employee);

        // When
        employeeRepository.delete(saved);
        entityManager.flush();

        // Then
        Employee found = entityManager.find(Employee.class, saved.getId());
        assertThat(found).isNull();
    }

    @Test
    void deleteById_ShouldRemoveEmployee() {
        // Given
        Employee employee = new Employee();
        employee.setName("David Lee");
        employee.setEmail("david.lee@example.com");
        employee.setDepartment("Sales");
        employee.setSalary(55000.0);

        Employee saved = entityManager.persistAndFlush(employee);

        // When
        employeeRepository.deleteById(saved.getId());
        entityManager.flush();

        // Then
        Employee found = entityManager.find(Employee.class, saved.getId());
        assertThat(found).isNull();
    }

    @Test
    void count_ShouldReturnCorrectCount() {
        // Given - clean slate
        employeeRepository.deleteAll();
        entityManager.flush();

        Employee employee1 = new Employee();
        employee1.setName("Employee 1");
        employee1.setEmail("emp1@example.com");

        Employee employee2 = new Employee();
        employee2.setName("Employee 2");
        employee2.setEmail("emp2@example.com");

        entityManager.persist(employee1);
        entityManager.persist(employee2);
        entityManager.flush();

        // When
        long count = employeeRepository.count();

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void existsById_ShouldReturnTrue_WhenEmployeeExists() {
        // Given
        Employee employee = new Employee();
        employee.setName("Test Employee");
        employee.setEmail("test@example.com");

        Employee saved = entityManager.persistAndFlush(employee);

        // When
        boolean exists = employeeRepository.existsById(saved.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsById_ShouldReturnFalse_WhenEmployeeDoesNotExist() {
        // When
        boolean exists = employeeRepository.existsById(999L);

        // Then
        assertThat(exists).isFalse();
    }
}