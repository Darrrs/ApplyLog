package com.example.hireme.service.impl;

import com.example.hireme.model.*;
import com.example.hireme.payload.JobApplicationRequest;
import com.example.hireme.repository.*;
import com.example.hireme.service.JobApplicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;

    // ✅ Constructor-based injection (recommended)
    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository,
                                     UserRepository userRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public JobApplication createApplication(JobApplicationRequest request, Long userId) {
        // 1️⃣ Fetch the user from DB (foreign key relation)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Build a new JobApplication
        JobApplication app = JobApplication.builder()
                .jobTitle(request.getJobTitle())
                .companyName(request.getCompanyName())
                .status(request.getStatus())
                .appliedDate(LocalDate.now())
                .user(user) // ✅ set user object, Hibernate auto-handles user_id
                .build();

        // 3️⃣ Save to DB
        return jobApplicationRepository.save(app);
    }

    @Override
    public List<JobApplication> getApplicationsByUser(Long userId) {
        return jobApplicationRepository.findByUserId(userId);
    }

    @Override
    public JobApplication updateApplication(Long id, JobApplicationRequest request, Long userId) {
        JobApplication app = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        
       


        // ✅ Only allow the same user to update their own app
        if (!app.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized update attempt");
        }

        app.setJobTitle(request.getJobTitle());
        app.setCompanyName(request.getCompanyName());
        app.setStatus(request.getStatus());

        
        

        return jobApplicationRepository.save(app);
    }

    @Override
    public void deleteApplication(Long id, Long userId) {
        JobApplication app = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!app.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized delete attempt");
        }

        jobApplicationRepository.delete(app);
    }

    @Override
    public List<JobApplication> getApplicationsByStatus(Long userId, ApplicationStatus status) {
        
        return jobApplicationRepository.findByUserIdAndStatus(userId, status);
    }
}
