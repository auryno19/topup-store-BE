package com.example.fufastore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.fufastore.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Query(value = """
            SELECT * FROM users
            WHERE email = ?1
            LIMIT 1
            """, nativeQuery = true)
    Users findByEmail(String email);

    @Query(value = """
            SELECT * FROM users
            WHERE username = ?1
            LIMIT 1
            """, nativeQuery = true)
    Users findByUsername(String username);
}
