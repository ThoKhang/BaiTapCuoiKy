package com.example.backend.controller.admin;

import com.example.backend.entity.CauHoi;
import com.example.backend.entity.DapAn;
import com.example.backend.service.AdminCauHoiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCauHoiController {

    private final AdminCauHoiService service;

    public AdminCauHoiController(AdminCauHoiService service) {
        this.service = service;
    }

    // ===== CÂU HỎI =====
    @GetMapping("/cau-hoi")
    public List<CauHoi> getAllCauHoi() {
        return service.getAllCauHoi();
    }

    @PostMapping("/cau-hoi")
    public CauHoi createCauHoi(@RequestBody CauHoi ch) {
        return service.createCauHoi(ch);
    }

    @DeleteMapping("/cau-hoi/{id}")
    public void deleteCauHoi(@PathVariable String id) {
        service.deleteCauHoi(id);
    }

    // ===== ĐÁP ÁN =====
    @GetMapping("/cau-hoi/{id}/dap-an")
    public List<DapAn> getDapAn(@PathVariable String id) {
        return service.getDapAnByCauHoi(id);
    }

    @PostMapping("/cau-hoi/{id}/dap-an")
    public DapAn createDapAn(@PathVariable String id,
                             @RequestBody DapAn da) {
        da.setMaCauHoi(id);
        return service.createDapAn(da);
    }

    @DeleteMapping("/dap-an/{id}")
    public void deleteDapAn(@PathVariable String id) {
        service.deleteDapAn(id);
    }
}
