package com.example.backend.controller;

import com.example.backend.service.CungCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cungco")
@CrossOrigin("*")
public class CungCoController {

    @Autowired
    private CungCoService service;

    // ============================
    // 1) API Lấy Tiến Độ Môn Học
    // ============================
    @GetMapping("/tiendo/{maNguoiDung}")
    public ResponseEntity<?> getTienDo(@PathVariable String maNguoiDung) {
        return ResponseEntity.ok(service.getTienDo(maNguoiDung));
    }

    // ==========================================
    // 2) API Lấy Danh Sách Các Bài Củng Cố Theo Môn Học
    // ==========================================
    @GetMapping("/monhoc/{maMonHoc}")
    public ResponseEntity<?> getDanhSachCungCo(@PathVariable String maMonHoc) {
        return ResponseEntity.ok(service.getCungCoByMonHoc(maMonHoc));
    }
}
