package com.example.nosqlcapstone.service;

import com.example.nosqlcapstone.exception.NotFoundException;
import com.example.nosqlcapstone.model.StatusDto;
import com.example.nosqlcapstone.model.Task;
import com.example.nosqlcapstone.model.TaskStatus;
import com.example.nosqlcapstone.model.User;
import com.example.nosqlcapstone.repository.TaskRepository;
import com.example.nosqlcapstone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        return user.orElseThrow(() -> new NotFoundException(String.format("User with id %1s not found", userId)));
    }

    @Override
    public List<Task> getTasksForUser(String userId) {
        return taskRepository.findByUserId(UUID.fromString(userId));
    }

    @Override
    public Task getTask(String userId, String taskId) {
        Optional<Task> task = taskRepository.findByUserIdAndTaskId(UUID.fromString(userId), UUID.fromString(taskId));
        return task.orElseThrow(() -> new NotFoundException(String.format("Task with id %1s not found for user with id %2s",taskId, userId)));
    }

    @Override
    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        log.debug(String.format("Task Service; createUser saved user = %1s", savedUser));
        return savedUser;
    }

    @Override
    public Task createTask(String userId, Task task) {
        if(task.getUserId() == null) {
            task.setUserId(UUID.fromString(userId));
        }
        if(userId == null || !userId.equals(task.getUserId().toString())) {
            throw new RuntimeException(String.format("Invalid userId in the task %1s", task));
        }
        Task savedTask = taskRepository.save(task);
        log.debug(String.format("Task Service; createTask saved task = 1%s", savedTask));
        return savedTask;
    }

    @Override
    public Task updateStatus(String userId, String taskId, StatusDto newStatus) {
        Optional<Task> task = taskRepository.findByUserIdAndTaskId(UUID.fromString(userId), UUID.fromString(taskId));

        log.debug(String.format("Task Service; updateStatus found task = 1%s", task.orElse(new Task())));
        task.ifPresent(task1 -> {
            task1.setStatus(TaskStatus.valueOf(newStatus.getStatus().toUpperCase()));
            taskRepository.save(task1);
            log.debug(String.format("Task Service; updateStatus updated task to = 1%s", task));
        });
        return task.orElseThrow(() -> new NotFoundException(String.format("Task with id %1s not found for user with id %2s",taskId, userId)));
    }

    @Override
    public void deleteTask(String userId, String taskId) {
        taskRepository.deleteByUserIdAndTaskId(UUID.fromString(userId), UUID.fromString(taskId));
    }
}
