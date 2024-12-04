package com.example.nosqlcapstone.controller;

import com.example.nosqlcapstone.model.StatusDto;
import com.example.nosqlcapstone.model.Task;
import com.example.nosqlcapstone.model.User;
import com.example.nosqlcapstone.service.TaskService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.debug("Task controller; getAllUsers");
        return taskService.getAllUsers();
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable String userId) {
        log.debug(String.format("Task controller; getUser with id = %1s", userId));
        return taskService.getUser(userId);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody User user, HttpServletResponse response) {
        log.debug(String.format("Task controller; createUser for user = %1s", user.toString()));
        User createdUser = taskService.createUser(user);
        response.addHeader("Location", String.format("/users/%1s", createdUser.getUserId()));
    }

    @PostMapping("/users/{userId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@PathVariable String userId, @RequestBody Task task, HttpServletResponse response) {
        log.debug(String.format("Task controller; create task for userId = %1s and task = %2s", userId, task.toString()));
        Task created = taskService.createTask(userId, task);
        response.addHeader("Location", String.format("/users/%1s/tasks/%2s", userId, created.getTaskId()));
    }

    @GetMapping("/users/{userId}/tasks")
    public List<Task> getTasksForUser(@PathVariable String userId) {
        log.debug(String.format("Task controller; getTasksForUser with userId = %1s", userId));
        return taskService.getTasksForUser(userId);
    }

    @GetMapping("/users/{userId}/tasks/{taskId}")
    public Task getTask(@PathVariable String userId, @PathVariable String taskId) {
        log.debug(String.format("Task controller; getTask with userId = %1s and taskId = %2s", userId, taskId));
        return taskService.getTask(userId, taskId);
    }

    @PatchMapping("/users/{userId}/tasks/{taskId}")
    public Task updateStatus(@PathVariable String userId, @PathVariable String taskId, @RequestBody StatusDto newStatus) {
        log.debug(String.format("Task controller; updateStatus with userId = %1s and taskId = %2s and new status = %3s", userId, taskId, newStatus.getStatus()));
        return taskService.updateStatus(userId, taskId, newStatus);
    }

    @DeleteMapping("/users/{userId}/tasks/{taskId}")
    public void deleteTask(@PathVariable String userId, @PathVariable String taskId) {
        log.debug(String.format("Task controller; deleteTask with userId = %1s and taskId = %2s", userId, taskId));
        taskService.deleteTask(userId, taskId);
    }

}
