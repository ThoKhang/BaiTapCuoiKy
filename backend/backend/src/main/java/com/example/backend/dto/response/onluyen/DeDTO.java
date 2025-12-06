/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.dto.response.onluyen;

/**
 *
 * @author ADMIN
 */
public class DeDTO {
    public String maHoatDong;
    public String tieuDe;
    public Integer soCauHoi;
    public boolean daLam;

    public DeDTO() {
    }

    public DeDTO(String maHoatDong, String tieuDe, Integer soCauHoi, boolean daLam) {
        this.maHoatDong = maHoatDong;
        this.tieuDe = tieuDe;
        this.soCauHoi = soCauHoi;
        this.daLam = daLam;
    }

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

    public Integer getSoCauHoi() {
        return soCauHoi;
    }

    public void setSoCauHoi(Integer soCauHoi) {
        this.soCauHoi = soCauHoi;
    }

    public boolean isDaLam() {
        return daLam;
    }

    public void setDaLam(boolean daLam) {
        this.daLam = daLam;
    }
    
}

