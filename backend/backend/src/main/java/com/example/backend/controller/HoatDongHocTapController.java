/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.controller;

import com.example.backend.service.HoatDongHocTapService;
import com.example.backend.service.IService.IHoatDongHocTapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api/hoatdong")
public class HoatDongHocTapController {

    @Autowired
    private IHoatDongHocTapService service;

    // ===== API 1: Tổng quan =====
    @GetMapping("/on-luyen/summary")
    public ResponseEntity<?> summary(@RequestParam String maNguoiDung) {
        return ResponseEntity.ok(service.getTongQuan(maNguoiDung));
    }

    // ===== API 2: Danh sách đề =====
    @GetMapping("/on-luyen/ds")
    public ResponseEntity<?> danhSach(
        @RequestParam String loai,
        @RequestParam String maNguoiDung
    ) {
        return ResponseEntity.ok(service.getDanhSachDe(loai, maNguoiDung));
    }

//    // ===== API 3: Chi tiết đề =====
//    @GetMapping("/on-luyen/cau-hoi")
//    public ResponseEntity<?> cauHoi(@RequestParam String maHoatDong) {
//        return ResponseEntity.ok(service.getChiTietDe(maHoatDong));
//    }
}


