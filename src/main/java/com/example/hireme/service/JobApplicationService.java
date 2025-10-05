package com.example.hireme.service;

import com.example.hireme.model.ApplicationStatus;
import com.example.hireme.model.JobApplication;
import com.example.hireme.payload.JobApplicationRequest;

import java.util.List;

public interface JobApplicationService {
    // create a new job application
    JobApplication createApplication(JobApplicationRequest request, Long userId);

    // get all applications for a specific user
    List<JobApplication> getApplicationsByUser(Long userId);

    // update an application
    JobApplication updateApplication(Long id, JobApplicationRequest request, Long userId);

    // delete an application
    void deleteApplication(Long id, Long userId);

    List<JobApplication> getApplicationsByStatus(Long userId, ApplicationStatus status);
}
