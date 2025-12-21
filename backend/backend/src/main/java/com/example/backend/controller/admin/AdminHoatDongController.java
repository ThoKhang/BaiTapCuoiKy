package com.example.backend.controller.admin;

import com.example.backend.entity.HoatDongHocTap;
import com.example.backend.service.AdminHoatDongService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/hoat-dong")
@PreAuthorize("hasRole('ADMIN')")
public class AdminHoatDongController {

    private final AdminHoatDongService service;

    public AdminHoatDongController(AdminHoatDongService service) {
        this.service = service;
    }

    @GetMapping
    public List<HoatDongHocTap> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public HoatDongHocTap getOne(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public HoatDongHocTap create(@RequestBody HoatDongHocTap hd) {
        return service.create(hd);
    }

    @PutMapping("/{id}")
    public HoatDongHocTap update(@PathVariable String id,
                                 @RequestBody HoatDongHocTap hd) {
        return service.update(id, hd);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    // Gán câu hỏi
    @PostMapping("/{id}/cau-hoi")
    public void addCauHoi(@PathVariable String id,
                          @RequestBody Map<String, Object> body) {

        String maCauHoi = (String) body.get("maCauHoi");
        int thuTu = (int) body.get("thuTu");

        service.addCauHoi(id, maCauHoi, thuTu);
    }

}
