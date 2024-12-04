package com.example.nosqlcapstone.service;

import com.example.nosqlcapstone.model.StatusDto;
import com.example.nosqlcapstone.model.Task;
import com.example.nosqlcapstone.model.User;

import java.util.List;

public interface TaskService {
    List<User> getAllUsers();
    User getUser(String userId);
    List<Task> getTasksForUser(String userId);

    Task getTask(String userId, String taskId);

    User createUser(User user);

    Task createTask(String userId, Task task);

    Task updateStatus(String userId, String taskId, StatusDto newStatus);

    void deleteTask(String userId, String taskId);
}
