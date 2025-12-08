package com.example.backend.controller;

import com.example.backend.service.LyThuyetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lythuyet")
@CrossOrigin("*")
public class LyThuyetController {

    @Autowired
    private LyThuyetService service;

    @GetMapping("/tiendo/{maNguoiDung}")
    public ResponseEntity<?> getTienDo(@PathVariable String maNguoiDung) {
        return ResponseEntity.ok(service.getTienDo(maNguoiDung));
    }

    @GetMapping("/monhoc/{maMonHoc}")
    public ResponseEntity<?> getDanhSachLyThuyet(@PathVariable String maMonHoc) {
        return ResponseEntity.ok(service.getLyThuyetByMonHoc(maMonHoc));
    }

    @GetMapping("/dalams")
    public ResponseEntity<?> getLyThuyetDaLam(
            @RequestParam String maMonHoc,
            @RequestParam String maNguoiDung) {
        return ResponseEntity.ok(service.getLyThuyetDaLam(maMonHoc, maNguoiDung));
    }

    @PostMapping("/hoanthanh")
    public ResponseEntity<?> hoanThanh(
            @RequestParam String maNguoiDung,
            @RequestParam String maHoatDong,
            @RequestParam int diem) {  // Loại soCauDung, tongCauHoi
        service.hoanThanhHoatDong(maNguoiDung, maHoatDong, diem);
        return ResponseEntity.ok("HOANTHANH");
    }
}