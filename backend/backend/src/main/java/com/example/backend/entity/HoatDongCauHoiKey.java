/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author ADMIN
 */
@Embeddable
public class HoatDongCauHoiKey implements java.io.Serializable {

    @Column(name = "MaHoatDong", length = 5)
    private String maHoatDong;

    @Column(name = "MaCauHoi", length = 5)
    private String maCauHoi;

    public HoatDongCauHoiKey() {
    }

    public HoatDongCauHoiKey(String maHoatDong, String maCauHoi) {
        this.maHoatDong = maHoatDong;
        this.maCauHoi = maCauHoi;
    }

    public String getMaHoatDong() {
        return maHoatDong;
    }

    public void setMaHoatDong(String maHoatDong) {
        this.maHoatDong = maHoatDong;
    }

    public String getMaCauHoi() {
        return maCauHoi;
    }

    public void setMaCauHoi(String maCauHoi) {
        this.maCauHoi = maCauHoi;
    }
    

}
