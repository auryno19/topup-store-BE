package com.example.fufastore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fufastore.model.Game;
import com.example.fufastore.repository.GameRepository;
import com.example.fufastore.util.ApiResponse;
import com.example.fufastore.util.ResponseUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("api/game")
public class ApiGameController {

    @Autowired
    private GameRepository gameRepository;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Object>> getAllGame() {
        try {
            List<Game> games = gameRepository.findIsActive();
            if (games.isEmpty()) {
                return ResponseUtil.generateSuccessResponse("List Game not found.", null);
            }
            return ResponseUtil.generateSuccessResponse("List Game retrieved successfully", games);
        } catch (Exception e) {
            return ResponseUtil.generateErrorResponse("Retrieved list game failed", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
