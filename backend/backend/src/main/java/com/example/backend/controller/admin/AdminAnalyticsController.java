package com.example.backend.controller.admin;

import com.example.backend.service.AdminAnalyticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/analytics")
@PreAuthorize("hasRole('ADMIN')")

public class AdminAnalyticsController {

    private final AdminAnalyticsService service;

    public AdminAnalyticsController(AdminAnalyticsService service) {
        this.service = service;
    }
    private LocalDateTime parseFrom(String fromDate) {
        return LocalDate.parse(fromDate).atStartOfDay();
    }
    private LocalDateTime parseTo(String toDate) {
        return LocalDate.parse(toDate).atTime(23,59,59);
    }

    @GetMapping("/overview")
    public Map<String, Object> overview(
            @RequestParam String fromDate,
            @RequestParam String toDate
    ) {
        return service.overview(parseFrom(fromDate), parseTo(toDate));
    }

    @GetMapping("/activities")
    public Map<String, Object> activities(
            @RequestParam String fromDate,
            @RequestParam String toDate
    ) {
        return service.activities(parseFrom(fromDate), parseTo(toDate));
    }

    @GetMapping("/subjects")
    public Map<String, Object> subjects(
            @RequestParam String fromDate,
            @RequestParam String toDate
    ) {
        return service.subjects(parseFrom(fromDate), parseTo(toDate));
    }

    @GetMapping("/students")
    public Map<String, Object> students(
            @RequestParam String fromDate,
            @RequestParam String toDate
    ) {
        return service.students(parseFrom(fromDate), parseTo(toDate));
    }
}
