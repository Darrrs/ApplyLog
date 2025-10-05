package com.example.hireme.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hireme.model.JobApplication;
import com.example.hireme.payload.JobApplicationRequest;
import com.example.hireme.repository.UserRepository;
import com.example.hireme.security.JwtUtil;
import com.example.hireme.service.JobApplicationService;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/applications")
public class JobApplicationController {

    private final JwtUtil jwtUtil;
    private final UserRepository UserRepository;
    private final JobApplicationService jobApplicationService;
    public JobApplicationController(JobApplicationService jobApplicationService, UserRepository UserRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.jobApplicationService = jobApplicationService;
        this.UserRepository = userRepository;
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

    @PostMapping
    public ResponseEntity<JobApplication> create(@RequestBody JobApplicationRequest request,
                                             HttpServletRequest httpRequest) {
        Long userId = extractUserIdFromRequest(httpRequest);
        return ResponseEntity.ok(jobApplicationService.createApplication(request, userId));
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> getUserApplications(HttpServletRequest httpRequest) {
        //Long userId = Long.parseLong(principal.getName());
        Long userId = extractUserIdFromRequest(httpRequest);
        return ResponseEntity.ok(jobApplicationService.getApplicationsByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplication> update(@PathVariable Long id, @RequestBody JobApplicationRequest request, HttpServletRequest httpRequest) {
        Long userId = extractUserIdFromRequest(httpRequest);
        // User user = UserRepository.findByEmail(email)
        //         .orElseThrow(() -> new RuntimeException("User not found"));

        JobApplication updated = jobApplicationService.updateApplication(id, request, userId);
        return ResponseEntity.ok(updated);        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JobApplication> delete(@PathVariable Long id, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        jobApplicationService.deleteApplication(id, userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobApplication>> findByUserIdAndStatus(Principal principal, @PathVariable String status) {

        long userId = Long.parseLong(principal.getName());
        List<JobApplication> applications = jobApplicationService.getApplicationsByStatus(userId, Enum.valueOf(com.example.hireme.model.ApplicationStatus.class, status.toUpperCase()));
        return ResponseEntity.ok(applications);
    }
    
    
}
