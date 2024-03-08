package ru.andryss.rutube.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.andryss.rutube.security.JwtFilter;

import static org.springframework.http.HttpMethod.*;
import static ru.andryss.rutube.model.Role.MODERATOR;
import static ru.andryss.rutube.model.Role.USER;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService, JwtFilter jwtFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/ping").permitAll()
                        .requestMatchers(POST, "/api/videos:new").hasRole(USER.name())
                        .requestMatchers(PUT, "/api/videos/*").hasRole(USER.name())
                        .requestMatchers(GET, "/api/videos/*/status").hasRole(USER.name())
                        .requestMatchers(POST, "/api/videos/*:publish").hasRole(USER.name())
                        .requestMatchers(GET, "/api/videos").permitAll()
                        .requestMatchers(GET, "/api/videos/*").permitAll()
                        .requestMatchers(POST, "/api/comments").hasRole(USER.name())
                        .requestMatchers(GET, "/api/comments").permitAll()
                        .requestMatchers(POST, "/api/reactions").hasRole(USER.name())
                        .requestMatchers(GET, "/api/reactions").permitAll()
                        .requestMatchers(GET, "/api/reactions/my").hasRole(USER.name())
                        .requestMatchers(GET, "/api/moderation/next").hasRole(MODERATOR.name())
                        .requestMatchers(POST, "/api/moderation").hasRole(MODERATOR.name())
                        .requestMatchers(PUT, "/api/sources/*").hasRole(USER.name())
                        .requestMatchers(GET, "/api/sources/*").permitAll()
                        .anyRequest().permitAll()
                )
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> response.setStatus(HttpStatus.FORBIDDEN.value());
    }
}
