package com.group6.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                .csrf().disable() // 如果不需要 CSRF 保护，可以禁用
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 允许预检请求
//                        .requestMatchers("/bill/add").permitAll() // 开放 /bill/add 路由
//                        .anyRequest().authenticated() // 其他路由需要认证
                                .anyRequest().permitAll()
                );
//                .httpBasic(); // 如果需要简单的 HTTP 基础认证

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
