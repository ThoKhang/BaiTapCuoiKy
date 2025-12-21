/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.controller;

import com.example.backend.dto.response.toanTiengVietGiaiTriResponse;
import com.example.backend.service.IService.IHoatDongHocTap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api/")
@CrossOrigin("*")
public class HoatDongHocTapController {
    @Autowired
    private IHoatDongHocTap hoatDongHocTap;
    @GetMapping("toan-tv-gtri")
    public List<toanTiengVietGiaiTriResponse> getToanTVGtri(){
        List<toanTiengVietGiaiTriResponse> responses= hoatDongHocTap.toanTVGTri();
        if(responses==null)
            return null;
        return responses;
    }
}
