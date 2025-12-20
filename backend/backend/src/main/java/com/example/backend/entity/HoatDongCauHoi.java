package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HoatDong_CauHoi")
@IdClass(HoatDongCauHoiId.class)
public class HoatDongCauHoi {

    @Id
    @Column(name = "MaHoatDong", length = 5)
    private String maHoatDong;

    @Id
    @Column(name = "MaCauHoi", length = 6)
    private String maCauHoi;

    @Column(name = "ThuTu")
    private int thuTu;

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

    public int getThuTu() {
        return thuTu;
    }

    public void setThuTu(int thuTu) {
        this.thuTu = thuTu;
    }
}
