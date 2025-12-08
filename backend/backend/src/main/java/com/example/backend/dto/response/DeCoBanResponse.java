/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.dto.response;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public class DeCoBanResponse {
    private String maHoatDong;
    private String tieuDe;
    private List<CauHoiResponse> danhSachCauHoi;

    public String getMaHoatDong() {
        return maHoatDong;
    }

    public void setMaHoatDong(String maHoatDong) {
        this.maHoatDong = maHoatDong;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public List<CauHoiResponse> getDanhSachCauHoi() {
        return danhSachCauHoi;
    }

    public void setDanhSachCauHoi(List<CauHoiResponse> danhSachCauHoi) {
        this.danhSachCauHoi = danhSachCauHoi;
    }
    
}
