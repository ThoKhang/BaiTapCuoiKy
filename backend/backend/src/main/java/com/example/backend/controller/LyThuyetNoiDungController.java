package com.example.backend.controller;

import com.example.backend.service.LyThuyetNoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lythuyet-noidung")
@CrossOrigin("*")
public class LyThuyetNoiDungController {

    @Autowired
    private LyThuyetNoiDungService service;

    @GetMapping("/{maHoatDong}")
    public ResponseEntity<?> getNoiDungLyThuyet(@PathVariable String maHoatDong) {
        return ResponseEntity.ok(service.getNoiDungByHoatDong(maHoatDong));
    }
}