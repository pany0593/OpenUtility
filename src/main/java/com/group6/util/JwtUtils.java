package com.group6.util;

import com.group6.pojo.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JwtUtils {
    // 密钥
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 令牌有效期：1天
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    /**
     * 生成 JWT 令牌
     *
     * @param claims  自定义载荷
     * @param subject 主题（如用户名）
     * @return 生成的 JWT 字符串
     */
    public static String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)//用户id
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 验证 JWT 令牌并返回解析后的数据
     *
     * @param token JWT 字符串
     * @return Claims 载荷数据
     */
    public static Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Token has expired", e);
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("Unsupported JWT token", e);
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("Invalid JWT token format", e);
        } catch (SignatureException e) {
            throw new IllegalArgumentException("Invalid JWT signature", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Token is null or empty", e);
        }
    }

    /**
     * 解析 JWT 并返回载荷数据
     *
     * @param token JWT 字符串
     * @return 解析后的载荷数据
     */
    public static Map<String, Object> parseToken(String token) {
        return validateToken(token);
    }

    /**
     * 自定义 JWT 认证过滤器
     */
    public static class JwtAuthenticationFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

            System.out.println("[JwtAuthenticationFilter] Filter invoked."); // 过滤器被触发

            // 获取请求头中的 Authorization
            String authorizationHeader = request.getHeader("Authorization");
            System.out.println("[JwtAuthenticationFilter] Authorization Header: " + authorizationHeader);


            // 检查是否存在 Bearer Token
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7); // 截取 Bearer 后的 Token
                System.out.println("[JwtAuthenticationFilter] Extracted Token: " + token);

                try {
                    // 验证并解析 JWT
                    Claims claims = JwtUtils.validateToken(token);
                    String username = claims.getSubject(); // 获取用户名
                    String role = (String) claims.get("role"); // 获取角色

                    System.out.println("[JwtAuthenticationFilter] JWT validated successfully.");
                    System.out.println("[JwtAuthenticationFilter] Username: " + username + ", Role: " + role);

                    // 构建权限集合
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                    // 创建 Authentication 对象
                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    System.out.println("[JwtAuthenticationFilter] Authentication created: " + authentication);

                    // 设置到 SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("[JwtAuthenticationFilter] SecurityContext updated.");

                } catch (Exception e) {
                    System.err.println("[JwtAuthenticationFilter] JWT validation failed: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: Invalid or expired token");
                    return;
                }

            } else {
                System.out.println("[JwtAuthenticationFilter] No Bearer Token found in Authorization Header.");
            }

            // 继续执行过滤器链
            System.out.println("[JwtAuthenticationFilter] Proceeding with the filter chain.");
            filterChain.doFilter(request, response);
        }
    }


}

