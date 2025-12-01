// controller/LyThuyetController.java
package com.example.backend.controller;

import com.example.backend.dto.request.HoanThanhLyThuyetRequest;
import com.example.backend.dto.response.LyThuyetDetailResponse;
import com.example.backend.dto.response.LyThuyetItemResponse;
import com.example.backend.service.IService.ILyThuyetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lythuyet")
@CrossOrigin("*")
public class LyThuyetController {

    private final ILyThuyetService service;

    public LyThuyetController(ILyThuyetService service) {
        this.service = service;
    }

    @GetMapping("/danh-sach")
    public ResponseEntity<List<LyThuyetItemResponse>> layDanhSach(
            @RequestParam String email,
            @RequestParam String mon) {
        return ResponseEntity.ok(service.layDanhSachTheoMon(email, mon));
    }

    // THÊM MỚI – CHỖ NÀY LÀ LỖI CỦA BẠN!!!
    @GetMapping("/chi-tiet")
    public ResponseEntity<LyThuyetDetailResponse> layChiTiet(
            @RequestParam String maHoatDong,
            @RequestParam String email) {
        LyThuyetDetailResponse result = service.layChiTiet(email, maHoatDong);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/hoan-thanh")
    public ResponseEntity<String> hoanThanh(@RequestBody HoanThanhLyThuyetRequest request) {
        try {
            return ResponseEntity.ok(service.hoanThanh(request));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}