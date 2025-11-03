package com.reliaquest.api.service;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();

    List<Employee> searchEmployeesByName(String nameFragment);

    Employee getEmployeeById(String id);

    boolean deleteEmployeeByName(String name);

    Integer getMaxSalary();

    List<Employee> getTopTenEarningEmployeeNames();

    Employee createEmployee(EmployeeDto employee);
}
