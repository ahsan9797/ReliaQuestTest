package com.reliaquest.api.dao;

import com.reliaquest.api.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    List<Employee> findByNameContainingIgnoreCase(String name);

    void deleteByName(String name);

    Optional<Employee> findById(String id);
}
