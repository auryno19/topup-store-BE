package com.example.fufastore.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.fufastore.util.CustomAccessDeniedHandler;
import com.example.fufastore.util.CustomAuthenticateEntryPoint;

// import com.example.fufastore.service.CustomUserDetailService;

// import com.example.fufastore.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private JwtRequestFilter jwtRequestFilter;

        // @Autowired
        // private RoleCheckerFilter roleCheckFilter;

        // @Autowired
        // private CustomUserDetailService userDetailsService;
        @Autowired
        private CustomAccessDeniedHandler customAccessDeniedHandler;

        @Autowired
        private CustomAuthenticateEntryPoint customAuthenticateEntryPoint;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                // Authentication authentication =
                // SecurityContextHolder.getContext().getAuthentication();
                // System.out.println(authentication);
                // System.out.println("Cek 123");
                http.authorizeHttpRequests(
                                authorize -> authorize
                                                .requestMatchers("/api/game", "api/banner").permitAll()
                                                .requestMatchers("/api/banner/edit")
                                                .hasRole("Admin")
                                                .requestMatchers("/api/auth/login", "/api/auth/register")
                                                .permitAll().anyRequest()
                                                .authenticated())
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(customAuthenticateEntryPoint)
                                                .accessDeniedHandler(customAccessDeniedHandler));

                http.addFilterBefore(jwtRequestFilter,
                                UsernamePasswordAuthenticationFilter.class);
                // http.addFilterAfter(roleCheckFilter, JwtRequestFilter.class);
                return http.build();

        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowCredentials(true);
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
                configuration.setAllowedHeaders(Arrays.asList(
                                HttpHeaders.AUTHORIZATION,
                                HttpHeaders.CONTENT_TYPE,
                                HttpHeaders.ACCEPT));
                configuration.setAllowedMethods(Arrays.asList(
                                HttpMethod.GET.name(),
                                HttpMethod.POST.name(),
                                HttpMethod.PUT.name()));
                configuration.setMaxAge(3600L);
                configuration.setAllowedHeaders(
                                Arrays.asList("Authorization", "Content-Type", "Accept", "Authorization"));
                configuration.setExposedHeaders(Arrays.asList("Authorization"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // @Bean
        // public AuthenticationProvider authenticationProvider() {
        // DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // provider.setUserDetailsService(userDetailsService());
        // provider.setPasswordEncoder(passwordEncoder());
        // return provider;

        // }

        // @Autowired
        // public void configureGlobal(AuthenticationManagerBuilder auth) throws
        // Exception {
        // auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        // }
}
