package com.reliaquest.api.service;

import com.reliaquest.api.dao.EmployeeRepository;
import com.reliaquest.api.exception.ResourceNotFoundException;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements  EmployeeService{

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> searchEmployeesByName(String nameFragment) {
        List<Employee> employeeList = employeeRepository.findByNameContainingIgnoreCase(nameFragment);
        if(employeeList == null || employeeList.isEmpty()){
            throw  new ResourceNotFoundException("No employee available with name: "+ nameFragment);
        }
        return employeeList;
    }

    public Employee getEmployeeById(String id) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        return employeeOpt.orElseThrow(() -> new ResourceNotFoundException("Employee doesn't exist with id: "+ id));
    }

    public boolean deleteEmployeeByName(String name) {
        if(!employeeRepository.existsById(name)) {
            throw new ResourceNotFoundException("Employee doesn't exist with name: "+ name);
        }
        employeeRepository.deleteByName(name);
        return true;
    }

    public Integer getMaxSalary() {
        return employeeRepository.findAll().stream()
                .map(Employee::getSalary)
                .max(Comparator.naturalOrder())
                .orElse(0);
    }

    public List<Employee> getTopTenEarningEmployeeNames() {
        return employeeRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public Employee createEmployee(EmployeeDto employeeDto) {
        Employee employee = getEmployeeFromDto(employeeDto);
        return employeeRepository.save(employee);
    }

    private Employee getEmployeeFromDto(EmployeeDto employeeDto) {
        return new Employee(employeeDto.getName(), employeeDto.getSalary(), employeeDto.getAge(), employeeDto.getTitle(), employeeDto.getEmail());
    }
}
