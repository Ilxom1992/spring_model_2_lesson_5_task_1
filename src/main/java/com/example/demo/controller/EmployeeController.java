package com.example.demo.controller;

import com.example.demo.payload.Response;
import com.example.demo.payload.SalaryDto;
import com.example.demo.service.EmployeeService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.UUID;
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping
    public HttpEntity<?> findAll() {
        Response response = employeeService.findAllEmployees();
        return ResponseEntity.status(response.isStatus() ? 200 : 401).body(response);
    }
    // xodimning belgilangan oraliq vaqt bo’yicha ishga kelib-ketishi va bajargan tasklari haqida ma’lumot
    @GetMapping("/findOneByData")
    public HttpEntity<?> findOneByData(@RequestParam UUID employeeId,
                                       @RequestParam Timestamp startDateTime,
                                       @RequestParam Timestamp finishDateTime){
        Response response=employeeService.findOneByData(employeeId,startDateTime,finishDateTime);
        return ResponseEntity.status(response.isStatus()?200:401).body(response);
    }
    // Belgilagan yil va oy bo’yicha berilgan oyliklarni ko’rish
    @GetMapping("/salary/byMonthDay")
    public HttpEntity<?> getSalariesByMonth(@RequestParam String year,  @RequestParam Integer monthNumber) {
        Response response = employeeService.getSalariesByMonth(year, monthNumber);
        return ResponseEntity.status(response.isStatus() ? 200 : 401).body(response);
    }
    // Xodim ID bo’yicha  berilgan oyliklarni ko’rish
    @GetMapping("/salary/{id}")
    public HttpEntity<?> getSalariesByEmployeeId(@PathVariable UUID id) {
        Response response = employeeService.getSalariesByUserId(id);
        return ResponseEntity.status(response.isStatus() ? 200 : 401).body(response);
    }
    // Oylik maosh berish
    @PostMapping("/salary")
    public HttpEntity<?> payMonthly(@RequestBody SalaryDto salaryDto) {
        Response response = employeeService.payMonthly(salaryDto);
        return ResponseEntity.status(response.isStatus() ? 200 : 401).body(response);
    }
}
