package com.example.fufastore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.fufastore.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = """
            SELECT * FROM game WHERE status = true
            """, nativeQuery = true)
    List<Game> findIsActive();
}
