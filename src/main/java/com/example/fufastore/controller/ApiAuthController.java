package com.example.fufastore.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.fufastore.config.JwtConfig;
import com.example.fufastore.model.Users;
import com.example.fufastore.repository.UserRepository;
import com.example.fufastore.util.ApiResponse;
import com.example.fufastore.util.ResponseUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
// @CrossOrigin("*")
@RequestMapping("api/auth")
public class ApiAuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    @RequestMapping("login")
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody Map<String, String> request,
            HttpServletResponse response) {
        try {

            Map<String, String> error = new HashMap<>();
            if (request.get("email") == "" || request.get("email") == null) {
                error.put("email", "Email is required");
            }
            if (request.get("password") == "" || request.get("password") == null) {
                error.put("password", "Password is required");
            }
            if (error.size() > 0) {
                return ResponseUtil.generateErrorResponse("Login failed", error, HttpStatus.CONFLICT);
            }
            Users user = this.userRepository.findByEmail(request.get("email"));
            if (user != null) {
                if (passwordEncoder.matches(request.get("password"), user.getPassword())) {
                    String token = JWT.create()
                            .withSubject(user.getUsername())
                            .withClaim("role", user.getRoleId())
                            .withIssuedAt(new Date())
                            .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                            .sign(Algorithm.HMAC512(jwtConfig.getJwtSecret()));

                    Cookie cookie = new Cookie("token", token);
                    cookie.setHttpOnly(true);
                    cookie.setSecure(false);
                    cookie.setPath("/");
                    cookie.setMaxAge(3600);
                    response.addCookie(cookie);
                    // HttpHeaders headers = new HttpHeaders();
                    // headers.set("Authorization", "Bearer " + token);
                    // System.out.println(headers);
                    // return ResponseUtil.generateSuccessResponseWithHeaders("Loggin Success",
                    // null, headers);
                    return ResponseUtil.generateSuccessResponse("Login Success", null);
                } else {
                    return ResponseUtil.generateErrorResponse("Loggin failed", "Email or password doesnt match");
                }
            } else {
                return ResponseUtil.generateErrorResponse("Loggin failed", "Email or password doesnt exists");
            }
        } catch (Exception e) {

            return ResponseUtil.generateErrorResponse("Loggin failed", e.getMessage());
        }
    }

    @RequestMapping("register")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody Map<String, String> request) {
        try {
            Map<String, String> error = new HashMap<>();
            Users userExist = this.userRepository.findByEmail(request.get("email"));
            if (userExist != null) {
                error.put("email", "Email already exists");
            }
            if (!Pattern.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", request.get("email"))) {
                error.put("email", "Email Format incorrect");
            }
            if (request.get("email") == "" || request.get("email") == null) {
                error.put("email", "Email is required");
            }
            Users usernameExist = this.userRepository.findByUsername(request.get("username"));
            if (usernameExist != null) {
                error.put("username", "Username already exists");
            }
            if (request.get("username") == "" || request.get("username") == null) {
                error.put("username", "Username is required");
            }

            List<String> validatePassword = passwordCheker(request.get("password"));
            if (validatePassword.size() > 0) {
                error.put("password", validatePassword.toString());
            }
            if (request.get("password") == "" || request.get("password") == null) {
                error.put("password", "Password is required");
            }
            if (!request.get("passwordConfirmation").equals(request.get("password"))) {
                error.put("passwordConfirmation", "Password Confirmation doesn't match");
            }
            if (request.get("passwordConfirmation") == "" || request.get("passwordConfirmation") == null) {
                error.put("passwordConfirmation", "Password Confirmation is required");
            }

            if (error.size() > 0) {
                return ResponseUtil.generateErrorResponse("Register failed", error, HttpStatus.CONFLICT);
            }
            Users user = new Users();
            user.setEmail(request.get("email"));
            user.setUsername(request.get("username"));
            user.setStatus(true);
            user.setCreatedAt(new Date());
            user.setPassword(passwordEncoder.encode(request.get("password")));
            this.userRepository.save(user);
            return ResponseUtil.generateSuccessResponse("Register Success", null);
        } catch (Exception e) {
            return ResponseUtil.generateErrorResponse("Register failed", e.getMessage());
        }
    }

    private static List<String> passwordCheker(String str) {
        List<String> validatePassword = new ArrayList<>();

        if (str.length() < 8) {
            validatePassword.add("Password must be at least 8 characters long");
        }
        boolean isupper = false;
        boolean islower = false;
        boolean isnumeric = false;
        boolean isspecial = false;

        for (char charStr : str.toCharArray()) {
            if (Character.isUpperCase(charStr)) {
                isupper = true;
            }
            if (Character.isLowerCase(charStr)) {
                islower = true;
            }
            if (Character.isDigit(charStr)) {
                isnumeric = true;
            }
            if (Pattern.matches("[^a-zA-Z0-9]", String.valueOf(charStr))) {
                isspecial = true;
            }
        }

        if (!isupper) {
            validatePassword.add("Password must be contain uppercase");
        }
        if (!islower) {
            validatePassword.add("Password must be contain lowercase");
        }
        if (!isnumeric) {
            validatePassword.add("Password must be contain number");
        }
        if (!isspecial) {
            validatePassword.add("Password must be contain special character");
        }

        return validatePassword;
    }

}
