package com.pnc.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pnc.project.filters.AuthorizationFilter;
import com.pnc.project.filters.AuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

        private final AuthenticationFilter authenticationFilter;
        private final AuthorizationFilter authorizationFilter;

        public SecurityConfig(
                        AuthenticationFilter authenticationFilter,
                        AuthorizationFilter authorizationFilter) {
                this.authenticationFilter = authenticationFilter;
                this.authorizationFilter = authorizationFilter;
        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/api/auth/login",
                                                                "/api/auth/refresh",
                                                                "/api/forgot-password",
                                                                "/api/validate-reset-token",
                                                                "/api/reset-password",
                                                                "api/save",
                                                                "/notifications/**")
                                                .permitAll().anyRequest().authenticated())

                                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterAfter(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
