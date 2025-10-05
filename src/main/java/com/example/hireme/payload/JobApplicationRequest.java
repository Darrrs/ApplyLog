package com.example.hireme.payload;
import com.example.hireme.model.ApplicationStatus;

import lombok.Data;

@Data
public class JobApplicationRequest {
    private String jobTitle;
    private String companyName;
    private ApplicationStatus status;    
}
