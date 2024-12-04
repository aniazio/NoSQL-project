package com.example.nosqlcapstone.repository;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.example.nosqlcapstone.model.Task;
import org.springframework.data.cassandra.repository.Consistency;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends MapIdCassandraRepository<Task> {
    List<Task> findByUserId(UUID userId);
    Optional<Task> findByUserIdAndTaskId(UUID userId, UUID taskId);
    void deleteByUserIdAndTaskId(UUID userId, UUID taskId);

}
