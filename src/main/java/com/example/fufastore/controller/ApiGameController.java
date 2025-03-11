package com.example.fufastore.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("add")
    public ResponseEntity<ApiResponse<Object>> addGame(@RequestParam("file") MultipartFile file,
            @RequestPart("data") Game game) {
        try {
            Map<String, String> errors = new HashMap<>();
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                errors.put("image", "File must be an image.");
            }

            Long fileSize = file.getSize() / (1024 / 1024);

            if (fileSize > 2) {
                errors.put("image", "Uploaded file size must not exceed 2 MB.");
            }

            if (game.getName() == "" || game.getName() == null) {
                errors.put("name", "Name must not be empty");
            }

            if (game.getPublisher() == "" || game.getPublisher() == null) {
                errors.put("publisher", "Pubisher  must not be empty");
            }
            if (!errors.isEmpty()) {
                return ResponseUtil.generateErrorResponse("Validation error", errors, HttpStatus.CONFLICT);
            }

            game.setStatus(true);
            game.setSlug(toSlug(game.getName()));
            game.setImage(file.getBytes());
            gameRepository.save(game);
            return ResponseUtil.generateSuccessResponse("Save Data Success", null, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseUtil.generateErrorResponse("Save failed.", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String ascii = input.replaceAll("[^\\p{ASCII}]", "");

        String lowerCase = ascii.toLowerCase();
        String slug = lowerCase.replaceAll("[\\s+]", "-").replaceAll("[^a-z0-9-]", "").replaceAll("-+", "-")
                .replaceAll("^-|-$", "");

        return slug;
    }
}
