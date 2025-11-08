/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.backend.service.IService;

import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface IDeKiemTraService {
    // tất đề
    int getTongSoDeTheoLoai(String loaiDe);
    //đề theo loại ngd lm
    int getSoDeDaLamTheoLoai(Integer maNguoiDung, String loaiDe);
    // tiến độ
    Map<String, String> getTienDoOnLuyen(Integer maNguoiDung);
}
