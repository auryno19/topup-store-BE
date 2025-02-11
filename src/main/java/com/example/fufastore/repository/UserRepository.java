package com.example.fufastore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fufastore.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

}
