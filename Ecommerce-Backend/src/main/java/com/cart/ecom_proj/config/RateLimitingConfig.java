package com.cart.ecom_proj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class RateLimitingConfig implements WebMvcConfigurer {

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lastResetTimes = new ConcurrentHashMap<>();
    
    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    private static final long RESET_INTERVAL = 60000; // 1 minute

    @Bean
    public HandlerInterceptor rateLimitingInterceptor() {
        return new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String clientIp = getClientIpAddress(request);
                long currentTime = System.currentTimeMillis();
                
                // Reset counter if interval has passed
                if (shouldResetCounter(clientIp, currentTime)) {
                    requestCounts.put(clientIp, new AtomicInteger(0));
                    lastResetTimes.put(clientIp, currentTime);
                }
                
                // Check rate limit
                AtomicInteger count = requestCounts.computeIfAbsent(clientIp, k -> new AtomicInteger(0));
                int currentCount = count.incrementAndGet();
                
                if (currentCount > MAX_REQUESTS_PER_MINUTE) {
                    response.setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
                    response.setHeader("Retry-After", "60");
                    return false;
                }
                
                return true;
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor())
                .addPathPatterns("/api/**");
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    private boolean shouldResetCounter(String clientIp, long currentTime) {
        Long lastReset = lastResetTimes.get(clientIp);
        return lastReset == null || (currentTime - lastReset) > RESET_INTERVAL;
    }
}
