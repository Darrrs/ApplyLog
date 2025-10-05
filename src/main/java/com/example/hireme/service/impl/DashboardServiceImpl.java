package com.example.hireme.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.hireme.model.JobApplication;
import com.example.hireme.repository.JobApplicationRepository;
import com.example.hireme.repository.UserRepository;
import com.example.hireme.service.DashboardService;
@Service

public class DashboardServiceImpl implements DashboardService {

    private UserRepository userRepository;
    private JobApplicationRepository jobApplicationRepository;

    public DashboardServiceImpl(UserRepository userRepository, JobApplicationRepository jobApplicationRepository) {
        this.userRepository = userRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public Map<String, Object> getDashboardStats(Long userId) {
        // Implementation logic to gather and return dashboard statistics
        List<JobApplication> applications = jobApplicationRepository.findByUserId(userId);
        // For simplicity, returning an empty map for now
        int totalApplications = applications.size();
        int applied = 0;
        int interview = 0;
        int rejected = 0;
        int offer = 0;

        for (JobApplication app : applications) {
            switch (app.getStatus()) {
                case APPLIED -> applied++;
                case INTERVIEW -> interview++;
                case REJECTED -> rejected++;
                case OFFERED -> offer++;
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalApplications", totalApplications);
        stats.put("applied", applied);
        stats.put("interview", interview);
        stats.put("rejected", rejected);
        stats.put("offer", offer);

        return stats;

    }
}
