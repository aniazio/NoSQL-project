package com.example.nosqlcapstone.service;

import com.example.nosqlcapstone.exception.NotFoundException;
import com.example.nosqlcapstone.model.StatusDto;
import com.example.nosqlcapstone.model.Task;
import com.example.nosqlcapstone.model.TaskStatus;
import com.example.nosqlcapstone.model.User;
import com.example.nosqlcapstone.repository.TaskRepository;
import com.example.nosqlcapstone.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    TaskRepository taskRepository;
    @InjectMocks
    TaskServiceImpl service;
    User user1, user2;
    Task task1, task2;
    String description = "some description";
    String title1 = "Task1";
    String title2 = "Task2";
    String email = "mail1@gmail.com";
    String username = "user1";
    String userId, taskId;

    @BeforeEach
    void setUp() {
        user1 = new User(username, email);
        userId = user1.getUserId().toString();
        user2 = new User("otheruser", "dfskl@gds.com");
        task1 = new Task();
        taskId = task1.getTaskId().toString();
        task1.setUserId(user1.getUserId());
        task1.setTitle(title1);
        task1.setDescription(description);
        task2 = new Task();
        task2.setUserId(user1.getUserId());
        task2.setTitle(title2);
        task2.setDescription(description);
      }

    @Test
    void should_getAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = service.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(email, result.get(0).getEmail());
        assertEquals(username, result.get(0).getUsername());
      }

    @Test
    void should_getUser() {
        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user1));

        User result = service.getUser(userId);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(username, result.getUsername());
      }

    @Test
    void should_throwException_getUser() {
        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getUser(userId));
    }

    @Test
    void should_getTasksForUser() {
        when(taskRepository.findByUserId(UUID.fromString(userId))).thenReturn(List.of(task1, task2));

        List<Task> result = service.getTasksForUser(userId);

        assertEquals(2, result.size());
        assertEquals(description, result.get(0).getDescription());
        assertEquals(description, result.get(1).getDescription());
        assertEquals(title1, result.get(0).getTitle());
      }

    @Test
    void should_returnEmptyList_getTasksForUser() {
        when(taskRepository.findByUserId(UUID.fromString(userId))).thenReturn(List.of());

        List<Task> result = service.getTasksForUser(userId);

        assertEquals(0, result.size());
    }

    @Test
    void should_getTask() {
        when(taskRepository.findByUserIdAndTaskId(UUID.fromString(userId), UUID.fromString(taskId))).thenReturn(Optional.of(task1));

        Task result = service.getTask(userId, taskId);

        assertNotNull(result);
        assertEquals(title1, result.getTitle());
        assertEquals(description, result.getDescription());
      }

    @Test
    void should_throwException_getTask() {
        when(taskRepository.findByUserIdAndTaskId(UUID.fromString(userId), UUID.fromString(taskId))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getTask(userId, taskId));

    }

    @Test
    void should_createUser() {
        when(userRepository.save(user1)).thenReturn(user1);

        User result = service.createUser(user1);

        assertEquals(email, result.getEmail());
        assertEquals(username, result.getUsername());
      }

    @Test
    void should_createTask() {
        when(taskRepository.save(task2)).thenReturn(task2);

        Task result = service.createTask(userId, task2);

        assertEquals(title2, result.getTitle());
        assertEquals(description, result.getDescription());
      }

    @Test
    void should_throwException_wrongUserId_createTask() {
        assertThrows(RuntimeException.class, () -> service.createTask(UUID.randomUUID().toString(), task1));

        then(userRepository).shouldHaveNoInteractions();
    }

    @Test
    void should_updateStatus() {
        StatusDto status = new StatusDto("IN_PROGRESS");
        when(taskRepository.findByUserIdAndTaskId(UUID.fromString(userId), UUID.fromString(taskId))).thenReturn(Optional.of(task1));

        Task result = service.updateStatus(userId, taskId, status);

        assertNotNull(result);
        assertEquals(title1, result.getTitle());
        assertEquals(description, result.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
      }

    @Test
    void should_throwException_updateStatus_whenNotInDb() {
        StatusDto status = new StatusDto("IN_PROGRESS");
        when(taskRepository.findByUserIdAndTaskId(UUID.fromString(userId), UUID.fromString(taskId))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.updateStatus(userId, taskId, status));
    }

    @Test
    void should_deleteTask() {
       service.deleteTask(userId, taskId);

       then(taskRepository).should().deleteByUserIdAndTaskId(any(), any());
      }
}