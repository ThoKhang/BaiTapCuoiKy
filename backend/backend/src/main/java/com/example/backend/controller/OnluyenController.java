/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.example.backend.controller;

import com.example.backend.service.IService.IDeKiemTraService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api/onluyen")
@CrossOrigin(origins = "*")
public class OnluyenController {
    @Autowired
    private IDeKiemTraService deKiemTraService;
    
    @GetMapping("/{maNguoiDung}")
    public ResponseEntity<Map<String, String>> getTienDo(@PathVariable Integer maNguoiDung) {
        Map<String, String> tienDo = deKiemTraService.getTienDoOnLuyen(maNguoiDung);
        return ResponseEntity.ok(tienDo);
    }
}
