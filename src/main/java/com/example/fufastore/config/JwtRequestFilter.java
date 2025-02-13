package com.example.fufastore.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
// import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtRequestFilter(@Lazy UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        // System.out.println("JwtRequestFilter is called for: " +
        // request.getRequestURI());
        if (requestURI.equals("/api/game") || requestURI.equals("/api/auth/login")) {
            // Jika permintaan adalah ke /api/game atau /login, lanjutkan tanpa memeriksa
            // token
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader("Authorization");
        // Cookie[] cookies = request.getCookies();
        // String token = null;

        // if (cookies != null) {
        // for (Cookie cookie : cookies) {
        // if (cookie.getName().equals("token")) {
        // token = cookie.getValue();
        // break;
        // }
        // }
        // }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            // if (token != null) {

            try {
                JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwtSecret)).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String email = decodedJWT.getSubject();

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Token tidak valid atau kedaluwarsa");
                return;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Header Authorization tidak ditemukan");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
