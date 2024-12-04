package com.example.nosqlcapstone.model;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Task {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID taskId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID userId;
    private String title;
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TaskStatus status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    public Task() {
        taskId = Uuids.timeBased();
        status = TaskStatus.CREATED;
        createdAt = LocalDateTime.now();
    }
}
