package com.example.fufastore.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
    // @RequestMapping("login")
    // public ResponseEntity<ApiResponse<Object>> login(@RequestBody Map<String,
    // String> request,
    // HttpServletResponse response) {
    // try {
    // String token = JWT.create()
    // .withSubject("Auryno dfs")
    // .withClaim("role", "1")
    // .withIssuedAt(new Date())
    // .withExpiresAt(new Date(System.currentTimeMillis() + 3600))
    // .sign(Algorithm.HMAC512(jwtConfig.getJwtSecret()));
    // System.out.println("Token: " + token);
    // HttpHeaders headers = new HttpHeaders();
    // headers.set("Authorization", "Bearer " + token);
    // System.out.println(headers);
    // return ResponseUtil.generateSuccessResponseWithHeaders("Loggin Success",
    // null, headers);
    // } catch (Exception e) {

    // return ResponseUtil.generateErrorResponse("Loggin failed", e.getMessage());
    // }
    // }
}
