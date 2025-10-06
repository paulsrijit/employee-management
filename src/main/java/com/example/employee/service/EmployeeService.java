package com.example.employee.service;

import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    public Employee create(Employee emp) {
        return repo.save(emp);
    }

    public List<Employee> listAll() {
        return repo.findAll();
    }

    public Employee findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Employee update(Long id, Employee updated) {
        Employee e = findById(id);
        e.setName(updated.getName());
        e.setDepartment(updated.getDepartment());
        e.setEmail(updated.getEmail());
        e.setSalary(updated.getSalary());
        return repo.save(e);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
