package com.reliaquest.api.service;

import com.reliaquest.api.dto.EmployeeResponseDto;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.dto.RequestEmployeeDto;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();

    List<Employee> searchEmployeesByName(String nameFragment);

    Employee getEmployeeById(String id);

    boolean deleteEmployeeByName(String name);

    Integer getMaxSalary();

    List<Employee> getTopTenEarningEmployeeNames();

    Employee createEmployee(RequestEmployeeDto employee);
}
