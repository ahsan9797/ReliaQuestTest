package com.reliaquest.api.controller;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.service.EmployeeService;
import com.reliaquest.api.service.EmployeeServiceImpl;
import jakarta.validation.Valid;
import org.aspectj.apache.bcel.generic.LOOKUPSWITCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController implements IEmployeeController<Employee, EmployeeDto>{

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
        logger.info("Controller class initialised");
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("Api called to fetch all employees");
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        logger.info("Api called to fetch employees by name {}", searchString);
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.searchEmployeesByName(searchString));
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        logger.info("Api called to fetch employee with id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeeById(id));
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        logger.info("Api called to fetch highest salary employee");
        return ResponseEntity.ok(employeeService.getMaxSalary());
    }

    @Override
    public ResponseEntity<List<Employee>> getTopTenHighestEarningEmployeeNames() {
        logger.info("Api called to fetch top 10 earning employees");
        return ResponseEntity.ok(employeeService.getTopTenEarningEmployeeNames());
    }

    @Override
    public ResponseEntity<Employee> createEmployee(@Valid EmployeeDto employeeInput) {
        logger.info("Api called to create employee with name {}", employeeInput.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employeeInput));
    }

    @Override
    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String name) {
        logger.info("Api called to delete employee with name {}", name);
        return ResponseEntity.ok(String.valueOf(employeeService.deleteEmployeeByName(name)));
    }
}
