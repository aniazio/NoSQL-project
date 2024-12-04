package com.example.nosqlcapstone.repository;

import com.example.nosqlcapstone.model.User;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface UserRepository extends CassandraRepository<User, UUID> {

}
