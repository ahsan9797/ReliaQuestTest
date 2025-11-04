package com.reliaquest.api.service;

import com.reliaquest.api.aop.LogExecutionTime;
import com.reliaquest.api.dao.EmployeeRepository;
import com.reliaquest.api.dto.EmployeeResponseDto;
import com.reliaquest.api.exception.ResourceNotFoundException;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.dto.RequestEmployeeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    /**
     * This method returns all the employees available in the database
     * @return List<Employee>
     */
    @LogExecutionTime
    @Cacheable(value = "employeesAll", unless = "#result == null")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * This method return the employees those name matches/full match/fragment with the input string
     * @param nameFragment
     * @return
     */
    @LogExecutionTime
    @Cacheable(value = "employeesSearch", key = "#nameFragment", unless = "#result == null")
    public List<Employee> searchEmployeesByName(String nameFragment) {
        List<Employee> employeeList = employeeRepository.findByNameContainingIgnoreCase(nameFragment);
        if(employeeList == null || employeeList.isEmpty()){
            throw new ResourceNotFoundException("No employee found with name: "+ nameFragment);
        }
        return employeeList;
    }

    /**
     * This method returns the employee by id.
     * If employee available with id not present, it will return 404.
     * @param id
     * @return
     */
    @LogExecutionTime
    @Cacheable(value = "employees", key = "#id", unless = "#result == null")
    public Employee getEmployeeById(String id) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        return employeeOpt.orElseThrow(() -> new ResourceNotFoundException("Employee doesn't exist with id: "+ id));
    }

    /**
     * This method deletes the employees by name.
     * If employee with name doesn't exist, it returns 404.
     * @param name
     * @return
     */
    @LogExecutionTime
    @CacheEvict(value = {"employees", "employeesAll", "employeesSearch", "topTenEmployees"}, allEntries = true)
    public boolean deleteEmployeeByName(String name) {
        if(!employeeRepository.existsById(name)) {
            throw new ResourceNotFoundException("Employee doesn't exist with name: "+ name);
        }
        employeeRepository.deleteByName(name);
        return true;
    }

    /**
     * This method returns the maximum salary.
     * @return
     */
    @LogExecutionTime
    @Cacheable(value = "maxSalary", unless = "#result == null")
    public Integer getMaxSalary() {
        return employeeRepository.findAll().stream()
                .map(Employee::getSalary)
                .max(Comparator.naturalOrder())
                .orElse(0);
    }

    /**
     * This method returns the ten employees with highest salary in descending order.
     * @return
     */
    @LogExecutionTime
    @Cacheable(value = "topTenEmployees", unless = "#result == null")
    public List<Employee> getTopTenEarningEmployeeNames() {
        return employeeRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * This method creats an employee
     * @param requestEmployeeDto
     * @return
     */
    @LogExecutionTime
    @CacheEvict(value = {"employees", "employeesAll", "employeesSearch", "topTenEmployees", "maxSalary"}, allEntries = true)
    public Employee createEmployee(RequestEmployeeDto requestEmployeeDto) {
        Employee employee = getEmployeeFromDto(requestEmployeeDto);
        return employeeRepository.save(employee);
    }

    /**
     * This method returns the Employee object created from EmployeeDto
     * @param requestEmployeeDto
     * @return
     */
    private Employee getEmployeeFromDto(RequestEmployeeDto requestEmployeeDto) {
        return new Employee(requestEmployeeDto.getName(), requestEmployeeDto.getSalary(), requestEmployeeDto.getAge(), requestEmployeeDto.getTitle(), requestEmployeeDto.getEmail());
    }
}
