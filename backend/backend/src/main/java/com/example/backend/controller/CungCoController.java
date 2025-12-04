package com.example.backend.controller;

import com.example.backend.service.IService.CungCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cungco")
public class CungCoController {

    @Autowired
    private CungCoService service;

    @GetMapping("/tiendo/{maNguoiDung}")
    public ResponseEntity<?> getTienDo(@PathVariable String maNguoiDung) {
        return ResponseEntity.ok(service.getTienDo(maNguoiDung));
    }
}
