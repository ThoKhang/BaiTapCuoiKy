/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.controller;

import com.example.backend.dto.request.TienTrinhRequest;
import com.example.backend.dto.response.TienTrinhResponse;
import com.example.backend.service.IService.ITienTrinhService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api/tientrinh/")
public class TienTrinhController {
    @Autowired
    private ITienTrinhService tienTrinhSe;
    @PostMapping("onluyen")
    public void taoTienTrinh(@RequestBody TienTrinhRequest tienTrinh){
        if(tienTrinh.getMaHoatDong()!=null){
            tienTrinhSe.taoTienTrinh(tienTrinh);
        }
    }
    @GetMapping("socaudalam-onluyen")
    public List<TienTrinhResponse> getTienTrinh(@RequestParam String email,@RequestParam String tieuDe) {
        return tienTrinhSe.getCauDungAndDaLam(email, tieuDe);
    }
}
