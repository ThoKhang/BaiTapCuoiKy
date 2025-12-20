package com.example.backend.controller.admin;

import com.example.backend.entity.NguoiDung;
import com.example.backend.service.AdminNguoiDungService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminNguoiDungController {

    private final AdminNguoiDungService service;

    public AdminNguoiDungController(AdminNguoiDungService service) {
        this.service = service;
    }

    @GetMapping
    public List<NguoiDung> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public NguoiDung getOne(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public NguoiDung create(@RequestBody NguoiDung nd) {
        return service.create(nd);
    }

    @PutMapping("/{id}")
    public NguoiDung update(@PathVariable String id,
                            @RequestBody NguoiDung nd) {
        return service.update(id, nd);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PutMapping("/{id}/reset-password")
    public void resetPassword(@PathVariable String id,
                              @RequestBody String newPassword) {
        service.resetPassword(id, newPassword);
    }
}
