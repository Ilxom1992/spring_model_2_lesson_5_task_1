package com.example.demo.controller;

import com.example.demo.payload.Response;
import com.example.demo.payload.TaskDto;
import com.example.demo.service.TaskService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {
    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
@PostMapping
    public HttpEntity<?> addTask(@RequestBody TaskDto taskDto){
        Response response = taskService.addTask(taskDto);
        return ResponseEntity.status(response.isStatus() ? 202 : 401).body(response);
    }
    //
    @PostMapping("/progress")
    public HttpEntity<?> taskProgress(@RequestParam String taskCode,@RequestParam Integer taskStatus){
        Response response = taskService.taskProgress(taskCode,taskStatus);
        return ResponseEntity.status(response.isStatus() ? 202 : 401).body(response);
    }

    @GetMapping
    public HttpEntity<?> checkEmployeeTask(@RequestParam UUID employeeId, @RequestParam Integer taskStatus) {
        Response response = taskService.checkEmployeeTask(employeeId, taskStatus);
        return ResponseEntity.status(response.isStatus() ? HttpStatus.ACCEPTED : HttpStatus.UNAUTHORIZED).body(response);
    }
}
