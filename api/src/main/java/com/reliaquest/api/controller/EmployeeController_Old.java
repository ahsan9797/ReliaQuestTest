/*
package com.reliaquest.api.controller;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController implements IEmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees =  employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@RequestParam String name) {
        List<Employee> employees =  employeeService.searchEmployeesByName(name);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Employee employee= employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<Double> getHighestSalaryOfEmployees() {
        Double maxSalary =  employeeService.getMaxSalary();
        return ResponseEntity.ok(maxSalary);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> employees = employeeService.getTopTenEarningEmployeeNames();
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee newEmployee =  employeeService.createEmployee(employee);
        return ResponseEntity.ok(newEmployee);
    }

    @Override
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable String id) {
        boolean created =  employeeService.deleteEmployeeById(id);
        if(created){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
*/
