package com.group6.config;

import com.group6.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity // 确保启用 Web Security
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors() // 启用跨域配置
                .and()
                .csrf().disable() // 禁用 CSRF（如有需要，可开启）
                .authorizeHttpRequests(authorize -> authorize
                                // 登录和注册接口允许匿名访问
                                .requestMatchers("/users/login", "/users/register").permitAll()

//                         仅允许具有 `ADMIN` 和 `USER` 权限的用户访问
                                .requestMatchers("/users/profile").hasAnyAuthority("ADMIN", "USER")

//                         仅允许 `ADMIN` 权限用户访问
                                .requestMatchers("/users/admin", "/users/by-room","/users/admin/*").hasAuthority("ADMIN")

//                        .anyRequest().authenticated() // 默认需要认证
                                // 其他请求需要认证后访问
                                .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 设置无状态会话
                )
                .addFilterBefore(new JwtUtils.JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // 注册自定义过滤器


        System.out.println("[SecurityConfig] Security filter chain configured successfully.");
        return http.build();
    }





    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // 允许所有源访问
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 允许的方法
        configuration.setAllowedHeaders(List.of("*")); // 允许的头
        configuration.setAllowCredentials(false); // 是否允许携带凭证

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 应用于所有路由
        return source;
    }


}
