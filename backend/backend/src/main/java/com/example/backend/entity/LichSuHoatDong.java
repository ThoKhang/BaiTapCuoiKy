package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LichSuHoatDong")
public class LichSuHoatDong {

    @Id
    @Column(name = "MaLichSu", length = 8)
    private String maLichSu;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", nullable = false)
    private NguoiDung nguoiDung;

    @Column(name = "ThoiGian")
    private LocalDateTime thoiGian;

    @Column(name = "LoaiDiem")
    private String loaiDiem;

    @Column(name = "SoDiem")
    private int soDiem;

    @Column(name = "ChiTiet")
    private String chiTiet;

    // ===== Getter / Setter =====

    public String getMaLichSu() {
        return maLichSu;
    }

    public void setMaLichSu(String maLichSu) {
        this.maLichSu = maLichSu;
    }

    public NguoiDung getNguoiDung() {
        return nguoiDung;
    }

    public void setNguoiDung(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getLoaiDiem() {
        return loaiDiem;
    }

    public void setLoaiDiem(String loaiDiem) {
        this.loaiDiem = loaiDiem;
    }

    public int getSoDiem() {
        return soDiem;
    }

    public void setSoDiem(int soDiem) {
        this.soDiem = soDiem;
    }

    public String getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(String chiTiet) {
        this.chiTiet = chiTiet;
    }
}
