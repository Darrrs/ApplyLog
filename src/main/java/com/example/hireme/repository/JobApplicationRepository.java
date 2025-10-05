package com.example.hireme.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hireme.model.ApplicationStatus;
import com.example.hireme.model.JobApplication;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    
    // custom query: find applications by userId
    List<JobApplication> findByUserId(Long userId);
    List<JobApplication> findByUserIdAndStatus(Long userId, ApplicationStatus status);

}
