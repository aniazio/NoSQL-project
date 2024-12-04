package com.example.nosqlcapstone.controller;

import com.example.nosqlcapstone.model.StatusDto;
import com.example.nosqlcapstone.model.Task;
import com.example.nosqlcapstone.model.User;
import com.example.nosqlcapstone.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    TaskService service;
    @InjectMocks
    TaskController controller;
    MockMvc mockMvc;
    User user;
    Task task;
    StatusDto status;
    String email = "email@gmail.com";
    String title = "title";
    String userId = "dsfserw523322";
    String taskId = "453453242";
    static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        user = new User("username", email);
        user.setUserId(null);

        task = new Task();
        task.setTaskId(null);
        task.setStatus(null);
        task.setCreatedAt(null);
        task.setTitle(title);
        task.setDescription("description");

        status = new StatusDto("IN_PROGRESS");
      }

    @Test
    void should_getAllUsers() throws Exception {
        when(service.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].email", Matchers.equalTo(email)));
        then(service).should().getAllUsers();
      }

    @Test
    void should_getUser() throws Exception {
        when(service.getUser(userId)).thenReturn(user);

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", Matchers.equalTo(email)));
        then(service).should().getUser(userId);
      }

    @Test
    void should_createUser() throws Exception {
        when(service.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users").content(mapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        then(service).should().createUser(any(User.class));
      }

  @Test
  void should_createTask() throws Exception {
        when(service.createTask(any(), any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/users/" + userId + "/tasks").content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        then(service).should().createTask(eq(userId), any(Task.class));
      }

  @Test
  void should_getTasksForUser() throws Exception {
        when(service.getTasksForUser(userId)).thenReturn(List.of(task));

        mockMvc.perform(get("/users/" + userId + "/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].title", Matchers.equalTo(title)));
        then(service).should().getTasksForUser(userId);
      }

    @Test
    void should_getTask() throws Exception {
        when(service.getTask(userId, taskId)).thenReturn(task);

        mockMvc.perform(get("/users/" + userId + "/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", Matchers.equalTo(title)));
        then(service).should().getTask(userId, taskId);
      }

    @Test
    void should_updateStatus() throws Exception {
        when(service.updateStatus(anyString(), anyString(), any(StatusDto.class))).thenReturn(task);

    mockMvc
        .perform(
            patch("/users/" + userId + "/tasks/" + taskId)
                .content(mapper.writeValueAsString(status))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", Matchers.equalTo(title)));
        then(service).should().updateStatus(eq(userId), eq(taskId), eq(status));
      }

    @Test
    void should_deleteTask() throws Exception {
        mockMvc
                .perform(
                        delete("/users/" + userId + "/tasks/" + taskId))
                .andExpect(status().isOk());
        then(service).should().deleteTask(userId, taskId);
      }
}