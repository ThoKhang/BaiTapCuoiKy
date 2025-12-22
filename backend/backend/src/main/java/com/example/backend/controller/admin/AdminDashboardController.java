package com.example.backend.controller.admin;

import com.example.backend.dto.admin.DashboardResponse;
import com.example.backend.repository.TienTrinhHocTapRepository;
import com.example.backend.service.AdminDashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService service;
    private final TienTrinhHocTapRepository tienTrinhRepo;

    public AdminDashboardController(
            AdminDashboardService service,
            TienTrinhHocTapRepository tienTrinhRepo
    ) {
        this.service = service;
        this.tienTrinhRepo = tienTrinhRepo;
    }

    @GetMapping
    public DashboardResponse getDashboard() {
        return service.getDashboard();
    }


    @GetMapping("/charts")
    public Map<String, Object> getDashboardCharts(
            @RequestParam String fromDate,
            @RequestParam String toDate
    ) {
        LocalDateTime from = LocalDate.parse(fromDate).atStartOfDay();
        LocalDateTime to = LocalDate.parse(toDate).atTime(23,59,59);

        Map<String, Object> result = new HashMap<>();
        result.put("theoNgay", tienTrinhRepo.thongKeTheoNgay(from, to));
        result.put("theoLoai", tienTrinhRepo.thongKeTheoLoai(from, to));
        result.put("theoMon", tienTrinhRepo.thongKeTheoMon(from, to));

        return result;
    }
}
