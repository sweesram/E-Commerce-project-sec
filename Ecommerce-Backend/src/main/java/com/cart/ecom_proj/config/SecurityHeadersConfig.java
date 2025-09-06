package com.cart.ecom_proj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityHeadersConfig {

    @Bean
    public OncePerRequestFilter securityHeadersFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                          FilterChain filterChain) throws ServletException, IOException {
                
                // OWASP Top 10 Security Headers
                
                // 1. X-Content-Type-Options: Prevents MIME type sniffing
                response.setHeader("X-Content-Type-Options", "nosniff");
                
                // 2. X-Frame-Options: Prevents clickjacking attacks
                response.setHeader("X-Frame-Options", "DENY");
                
                // 3. X-XSS-Protection: Enables XSS filtering
                response.setHeader("X-XSS-Protection", "1; mode=block");
                
                // 4. Strict-Transport-Security: Enforces HTTPS
                response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
                
                // 5. Content-Security-Policy: Prevents XSS attacks
                response.setHeader("Content-Security-Policy", 
                    "default-src 'self'; " +
                    "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                    "style-src 'self' 'unsafe-inline'; " +
                    "img-src 'self' data: https:; " +
                    "font-src 'self'; " +
                    "connect-src 'self' https://dev-your-domain.auth0.com; " +
                    "frame-ancestors 'none'; " +
                    "base-uri 'self'; " +
                    "form-action 'self'");
                
                // 6. Referrer-Policy: Controls referrer information
                response.setHeader("Referrer-Policy", ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN.getPolicy());
                
                // 7. Permissions-Policy: Controls browser features
                response.setHeader("Permissions-Policy", 
                    "geolocation=(), " +
                    "microphone=(), " +
                    "camera=(), " +
                    "payment=(), " +
                    "usb=(), " +
                    "magnetometer=(), " +
                    "gyroscope=(), " +
                    "speaker=(), " +
                    "vibrate=(), " +
                    "fullscreen=(self), " +
                    "sync-xhr=()");
                
                // 8. Cache-Control: Prevents caching of sensitive data
                if (request.getRequestURI().contains("/api/")) {
                    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, proxy-revalidate");
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Expires", "0");
                }
                
                filterChain.doFilter(request, response);
            }
        };
    }
}
