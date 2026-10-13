package com.example.ExpenseClaim.Config;

import com.example.ExpenseClaim.Security.CustomAccessDeniedHandler;
import com.example.ExpenseClaim.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf ->csrf.disable())
                .exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler))
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/employee")
                        .permitAll()

                        .requestMatchers(HttpMethod.POST, "/expenses")
                        .hasAnyRole("EMPLOYEE","MANAGER","FINANCE")

                        .requestMatchers(HttpMethod.GET, "/expenses/my")
                        .hasAnyRole("EMPLOYEE","MANAGER","FINANCE")

                        .requestMatchers(HttpMethod.GET, "/expenses/pending")
                        .hasAnyRole("MANAGER","FINANCE")

                        .requestMatchers(HttpMethod.PUT, "/expenses/{id}/approve")
                        .hasRole("MANAGER")

                        .requestMatchers(HttpMethod.PUT, "/expenses/{id}/reject")
                        .hasRole("MANAGER")

                        .requestMatchers(HttpMethod.GET, "/expenses/approve")
                        .hasAnyRole("MANAGER","FINANCE")

                        .requestMatchers(HttpMethod.PUT, "/expenses/{id}/pay")
                        .hasRole("FINANCE")

                        .requestMatchers(HttpMethod.GET, "/expenses/filter")
                        .hasAnyRole("MANAGER","FINANCE")

                        .requestMatchers(HttpMethod.GET, "/expenses/category_total")
                        .hasAnyRole("MANAGER","FINANCE")

                        .requestMatchers(HttpMethod.GET, "/expenses/{id}")
                        .hasAnyRole("EMPLOYEE", "MANAGER", "FINANCE")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
