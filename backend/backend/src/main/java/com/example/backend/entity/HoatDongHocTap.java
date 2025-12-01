package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HoatDongHocTap")
public class HoatDongHocTap {

    @Id
    @Column(name = "MaHoatDong")
    private String maHoatDong;

    @Column(name = "TieuDe")
    private String tieuDe;

    @Column(name = "MaLoai")
    private String maLoai;

    @Column(name = "MoTa")
    private String moTa;

    @Column(name = "TongDiemToiDa")
    private Integer tongDiemToiDa;

    @Column(name = "MaMonHoc")
    private String maMonHoc;

    // Constructor rỗng
    public HoatDongHocTap() {}

    // Getters & Setters
    public String getMaHoatDong() { return maHoatDong; }
    public void setMaHoatDong(String maHoatDong) { this.maHoatDong = maHoatDong; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public Integer getTongDiemToiDa() { return tongDiemToiDa; }
    public void setTongDiemToiDa(Integer tongDiemToiDa) { this.tongDiemToiDa = tongDiemToiDa; }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }
}