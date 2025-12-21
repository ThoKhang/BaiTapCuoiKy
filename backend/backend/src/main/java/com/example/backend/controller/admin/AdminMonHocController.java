package com.example.backend.controller.admin;

import com.example.backend.entity.MonHoc;
import com.example.backend.service.AdminMonHocService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/mon-hoc")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMonHocController {

    private final AdminMonHocService monHocService;

    public AdminMonHocController(AdminMonHocService monHocService) {
        this.monHocService = monHocService;
    }

    // GET /api/admin/mon-hoc
    @GetMapping
    public List<MonHoc> getAll() {
        return monHocService.getAll();
    }

    // POST /api/admin/mon-hoc
    @PostMapping
    public MonHoc create(@RequestBody MonHoc monHoc) {
        return monHocService.create(monHoc);
    }

    // DELETE /api/admin/mon-hoc/{ma}
    @DeleteMapping("/{ma}")
    public void delete(@PathVariable String ma) {
        monHocService.delete(ma);
    }
}
