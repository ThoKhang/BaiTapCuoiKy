package com.example.backend.service;

import com.example.backend.repository.TienTrinhHocTapRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminAnalyticsService {
    private final TienTrinhHocTapRepository repo;
    public AdminAnalyticsService(TienTrinhHocTapRepository repo) {
        this.repo = repo;
    }

    public Map<String, Object> overview(LocalDateTime from, LocalDateTime to) {
        Map<String, Object> data = new HashMap<>();
        data.put("overview", repo.overview(from, to));
        return data;
    }

    public Map<String, Object> activities(LocalDateTime from, LocalDateTime to) {
        Map<String, Object> data = new HashMap<>();
        data.put("top", repo.topActivities(from, to, 5));
        data.put("bottom", repo.bottomActivities(from, to, 5));
        data.put("completion", repo.completionByActivity(from, to));
        data.put("avgTime", repo.avgTimeByActivity(from, to));
        return data;
    }

    public Map<String, Object> subjects(LocalDateTime from, LocalDateTime to) {
        Map<String, Object> data = new HashMap<>();
        data.put("subjects", repo.subjectPopularity(from, to));
        return data;
    }

    public Map<String, Object> students(LocalDateTime from, LocalDateTime to) {
        Map<String, Object> data = new HashMap<>();
        data.put("topStudents", repo.topStudents(from, to, 10));
        return data;
    }
}
