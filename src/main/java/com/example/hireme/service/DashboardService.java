package com.example.hireme.service;

import java.util.Map;

public interface DashboardService {
    Map<String, Object> getDashboardStats(Long userId);
}
