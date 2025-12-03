/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.controller;

import com.example.backend.dto.response.onluyen.ResponseOnLuyenDTO;
import com.example.backend.service.HoatDongHocTapService;
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
    private HoatDongHocTapService hoatDongService;

    @GetMapping("/on-luyen")
    public ResponseEntity<?> thongTinOnLuyen(@RequestParam String maNguoiDung) {
        ResponseOnLuyenDTO res = hoatDongService.layThongTinOnLuyen(maNguoiDung);
        return ResponseEntity.ok(res);
    }
}

