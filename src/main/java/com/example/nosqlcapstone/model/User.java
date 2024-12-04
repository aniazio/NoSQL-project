package com.example.nosqlcapstone.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @PrimaryKey
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID userId;
    private String username;
    private String email;

    public User(String username, String email) {
        this.userId = UUID.randomUUID();
        this.username = username;
        this.email = email;
    }

    public User() {
        userId = UUID.randomUUID();
    }
}
