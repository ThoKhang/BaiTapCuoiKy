package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HoatDongHocTap")
public class HoatDongHocTap {

    @Id
    @Column(name = "MaHoatDong")
    private String maHoatDong;

    @Column(name = "MaMonHoc")
    private String maMonHoc;

    @Column(name = "MaLoai")
    private String maLoai;

    @Column(name = "TieuDe")
    private String tieuDe;

    @Column(name = "MoTa")
    private String moTa;

    @Column(name = "TongDiemToiDa")
    private int tongDiemToiDa;

    //===== Getter & Setter =====
    public String getMaHoatDong() { return maHoatDong; }
    public void setMaHoatDong(String maHoatDong) { this.maHoatDong = maHoatDong; }

    public String getMaMonHoc() { return maMonHoc; }
    public void setMaMonHoc(String maMonHoc) { this.maMonHoc = maMonHoc; }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public int getTongDiemToiDa() { return tongDiemToiDa; }
    public void setTongDiemToiDa(int tongDiemToiDa) { this.tongDiemToiDa = tongDiemToiDa; }
}
