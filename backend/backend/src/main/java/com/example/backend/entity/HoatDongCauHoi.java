/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "HoatDong_CauHoi")
public class HoatDongCauHoi {

    @EmbeddedId
    private HoatDongCauHoiKey id;

    @ManyToOne
    @MapsId("maHoatDong")
    @JoinColumn(name = "MaHoatDong")
    private HoatDongHocTap hoatDong;

    @ManyToOne
    @MapsId("maCauHoi")
    @JoinColumn(name = "MaCauHoi")
    private CauHoi cauHoi;

    @Column(name = "ThuTu")
    private Integer thuTu;

    public HoatDongCauHoi() {}

    public HoatDongCauHoiKey getId() {
        return id;
    }

    public void setId(HoatDongCauHoiKey id) {
        this.id = id;
    }

    public HoatDongHocTap getHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(HoatDongHocTap hoatDong) {
        this.hoatDong = hoatDong;
    }

    public CauHoi getCauHoi() {
        return cauHoi;
    }

    public void setCauHoi(CauHoi cauHoi) {
        this.cauHoi = cauHoi;
    }

    public Integer getThuTu() {
        return thuTu;
    }

    public void setThuTu(Integer thuTu) {
        this.thuTu = thuTu;
    }
}
