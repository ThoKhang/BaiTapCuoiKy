/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.controller;

import com.example.backend.dto.response.OnLuyenResponse;
import com.example.backend.service.IService.IOnLuyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api/onluyen")
@CrossOrigin("*")
public class OnLuyenController {
    @Autowired
    private IOnLuyenService onLuyenService;
    @GetMapping()
    public OnLuyenResponse thongTinOnLuyen(@RequestParam String email){
        return onLuyenService.layThongTinOnLuyen(email);
    }
}
