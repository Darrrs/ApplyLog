package com.example.hireme.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hireme.security.JwtUtil;
import com.example.hireme.service.DashboardService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    
   
    private DashboardService dashboardService;
    private final JwtUtil jwtUtil;
    public DashboardController(DashboardService dashboardService, JwtUtil jwtUtil) {
        this.dashboardService = dashboardService;
        this.jwtUtil = jwtUtil;
    }   

    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }


    // Implement endpoints as needed
    @GetMapping("/stats")
    public ResponseEntity<Map<String,Object>> getMethodName(HttpServletRequest httpRequest) {
        Long userId = extractUserIdFromRequest(httpRequest);
        Map<String, Object> stats = dashboardService.getDashboardStats(userId);
        return ResponseEntity.ok(stats);
    }
    
}
