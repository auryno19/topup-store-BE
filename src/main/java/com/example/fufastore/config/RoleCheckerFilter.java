// package com.example.fufastore.config;

// import java.io.IOException;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import org.springframework.http.HttpStatus;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Component
// public class RoleCheckerFilter extends OncePerRequestFilter {

// private final Map<String, Map<String, List<String>>> rolePermissions = new
// HashMap<>();

// public RoleCheckerFilter() {
// rolePermissions.put("1", Map.of(
// "GET", List.of("/api/admin/**", "/api/user/**"),
// "POST", List.of("/api/admin/**", "/api/user/**"),
// "PUT", List.of("/api/admin/**"),
// "DELETE", List.of("/api/admin/**")));
// rolePermissions.put("2", Map.of(
// "GET", List.of("/api/user/**"),
// "POST", List.of("/api/user/**"),
// "PUT", List.of(), // Tidak ada akses untuk PUT
// "DELETE", List.of()));
// }

// @Override
// protected void doFilterInternal(HttpServletRequest request,
// HttpServletResponse response, FilterChain filterChain)
// throws ServletException, IOException {
// Authentication authentication =
// SecurityContextHolder.getContext().getAuthentication();

// if (authentication != null && authentication.isAuthenticated()) {
// // Mendapatkan peran pengguna
// String role = authentication.getAuthorities().stream()
// .findFirst()
// .map(grantedAuthority -> grantedAuthority.getAuthority())
// .orElse(null);

// String requestURI = request.getRequestURI();
// String method = request.getMethod();

// if (role != null && rolePermissions.containsKey(role)) {
// Map<String, List<String>> allowedMethods = rolePermissions.get(role);
// List<String> allowedUrls = allowedMethods.get(method);

// if (allowedUrls != null &&
// allowedUrls.stream().anyMatch(requestURI::matches)) {
// filterChain.doFilter(request, response);
// return;
// }
// }
// response.setStatus(HttpStatus.FORBIDDEN.value());
// response.getWriter().write("Access Denied: You do not have permission to
// access this resource.");
// return;
// }
// response.setStatus(HttpStatus.UNAUTHORIZED.value());
// response.getWriter().write("Access Denied: You must be authenticated to
// access this resource.");
// }
// }
