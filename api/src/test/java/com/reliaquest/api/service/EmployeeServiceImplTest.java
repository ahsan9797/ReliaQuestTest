package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import com.reliaquest.api.dao.EmployeeRepository;
import com.reliaquest.api.exception.ResourceNotFoundException;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees_ReturnsList() {
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", 50000, 30, "Developer", "alice@example.com"),
                new Employee("Bob", 60000, 35, "Manager", "bob@example.com")
        );
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();
        assertEquals(2, result.size());
        verify(employeeRepository).findAll();
    }

    @Test
    void testSearchEmployeesByName_ReturnsMatchingEmployees() {
        String search = "ali";
        List<Employee> filtered = List.of(new Employee("Alice", 50000, 30, "Developer", "alice@example.com"));
        when(employeeRepository.findByNameContainingIgnoreCase(search)).thenReturn(filtered);

        List<Employee> result = employeeService.searchEmployeesByName(search);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
        verify(employeeRepository).findByNameContainingIgnoreCase(search);
    }

    @Test
    void testGetEmployeeById_ExistingId_ReturnsEmployee() {
        String id = "123";
        Employee emp = new Employee("Alice", 50000, 30, "Developer", "alice@example.com");
        when(employeeRepository.findById(id)).thenReturn(Optional.of(emp));

        Employee result = employeeService.getEmployeeById(id);
        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(employeeRepository).findById(id);
    }

    @Test
    void testGetEmployeeById_NonExistingId_ReturnsNull() {
        String id = "999";
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(id);
        });
        verify(employeeRepository).findById(id);
    }

    @Test
    void testDeleteEmployeeByName_ExistingId_DeletesAndReturnsTrue() {
        String id = "123";
        when(employeeRepository.existsById(id)).thenReturn(true);
        doNothing().when(employeeRepository).deleteByName(id);

        boolean result = employeeService.deleteEmployeeByName(id);
        assertTrue(result);
        verify(employeeRepository).existsById(id);
        verify(employeeRepository).deleteByName(id);
    }

    @Test
    void testDeleteEmployeeByName_NonExistingId_ReturnsFalse() {
        String id = "999";
        when(employeeRepository.existsById(id)).thenReturn(false);

        boolean result = employeeService.deleteEmployeeByName(id);
        assertFalse(result);
        verify(employeeRepository).existsById(id);
        verify(employeeRepository, never()).deleteByName(anyString());
    }

    @Test
    void testGetMaxSalary_ReturnsMaxSalary() {
        List<Employee> employees = List.of(
                new Employee("Alice", 50000, 30, "Developer", "alice@example.com"),
                new Employee("Bob", 70000, 35, "Manager", "bob@example.com")
        );
        when(employeeRepository.findAll()).thenReturn(employees);

        Integer maxSalary = employeeService.getMaxSalary();
        assertEquals(70000, maxSalary);
        verify(employeeRepository).findAll();
    }

    @Test
    void testGetMaxSalary_EmptyList_ReturnsZero() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        Integer maxSalary = employeeService.getMaxSalary();
        assertEquals(0, maxSalary);
        verify(employeeRepository).findAll();
    }

    @Test
    void testGetTopTenEarningEmployeeNames_ReturnsTop10() {
        List<Employee> employees = new ArrayList<>();
        // Create 15 employees with increasing salary
        for (int i = 0; i < 15; i++) {
            employees.add(new Employee("Emp" + i, 1000 + i * 1000, 30+i, "Title"+i, "email"+i+"@example.com"));
        }
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> topTen = employeeService.getTopTenEarningEmployeeNames();
        assertEquals(10, topTen.size());
        assertEquals(1000 + 14*1000, topTen.get(0).getSalary());
        assertEquals(1000 + 5*1000, topTen.get(9).getSalary());
    }

    @Test
    void testCreateEmployee_SavesAndReturnsEmployee() {
        EmployeeDto dto = new EmployeeDto();
        dto.setName("New Emp");
        dto.setSalary(55000);
        dto.setAge(28);
        dto.setTitle("Dev");
        dto.setEmail("new.emp@example.com");

        Employee savedEmployee = new Employee("New Emp", 55000, 28, "Dev", "new.emp@example.com");

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(dto);
        assertNotNull(result);
        assertEquals("New Emp", result.getName());
        verify(employeeRepository).save(any(Employee.class));
    }
}
