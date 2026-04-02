package com.webscrap.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Máximo de peticiones por minuto por IP
    private static final int MAX_REQUESTS_PER_MINUTE = 10;

    // Almacena: IP -> [contador, timestamp]
    private final Map<String, RateLimitInfo> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String clientIP = getClientIP(request);

        if (isRateLimited(clientIP)) {
            response.setStatus(429); // Too Many Requests
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Demasiadas peticiones. Intenta de nuevo en 1 minuto.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRateLimited(String clientIP) {
        long currentTime = System.currentTimeMillis();

        requestCounts.compute(clientIP, (ip, info) -> {
            if (info == null || currentTime - info.windowStart > 60000) {
                // Nueva ventana de 1 minuto
                return new RateLimitInfo(currentTime, 1);
            } else {
                info.count++;
                return info;
            }
        });

        return requestCounts.get(clientIP).count > MAX_REQUESTS_PER_MINUTE;
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static class RateLimitInfo {
        long windowStart;
        int count;

        RateLimitInfo(long windowStart, int count) {
            this.windowStart = windowStart;
            this.count = count;
        }
    }
}
