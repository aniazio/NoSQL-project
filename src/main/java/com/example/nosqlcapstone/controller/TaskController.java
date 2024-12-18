package com.example.nosqlcapstone.controller;

import com.example.nosqlcapstone.model.StatusDto;
import com.example.nosqlcapstone.model.Task;
import com.example.nosqlcapstone.model.User;
import com.example.nosqlcapstone.service.TaskService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return taskService.getAllUsers();
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable String userId) {
        return taskService.getUser(userId);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Valid User user, HttpServletResponse response) {
        User createdUser = taskService.createUser(user);
        response.addHeader("Location", String.format("/users/%1s", createdUser.getUserId()));
    }

    @PostMapping("/users/{userId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@PathVariable String userId, @RequestBody @Valid Task task, HttpServletResponse response) {
        Task created = taskService.createTask(userId, task);
        response.addHeader("Location", String.format("/users/%1s/tasks/%2s", userId, created.getTaskId()));
    }

    @GetMapping("/users/{userId}/tasks")
    public List<Task> getTasksForUser(@PathVariable String userId) {
        return taskService.getTasksForUser(userId);
    }

    @GetMapping("/users/{userId}/tasks/{taskId}")
    public Task getTask(@PathVariable String userId, @PathVariable String taskId) {
        return taskService.getTask(userId, taskId);
    }

    @PatchMapping("/users/{userId}/tasks/{taskId}")
    public Task updateStatus(@PathVariable String userId, @PathVariable String taskId, @RequestBody @Valid StatusDto newStatus) {
        return taskService.updateStatus(userId, taskId, newStatus);
    }

    @DeleteMapping("/users/{userId}/tasks/{taskId}")
    public void deleteTask(@PathVariable String userId, @PathVariable String taskId) {
        taskService.deleteTask(userId, taskId);
    }

}
