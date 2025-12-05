package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TienTrinhHocTap")
public class TienTrinhHocTap {

    @Id
    @Column(name = "MaTienTrinh", length = 5)
    private String maTienTrinh;

    // FK -> NguoiDung
    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", nullable = false)
    private NguoiDung nguoiDung;

    // FK -> HoatDongHocTap
    @ManyToOne
    @JoinColumn(name = "MaHoatDong", nullable = false)
    private HoatDongHocTap hoatDong;

    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "NgayHoanThanh")
    private LocalDateTime ngayHoanThanh;

    @Column(name = "SoCauDung")
    private int soCauDung;

    @Column(name = "SoCauDaLam")
    private int soCauDaLam;

    @Column(name = "DiemDatDuoc")
    private int diemDatDuoc;

    @Column(name = "DaHoanThanh")
    private boolean daHoanThanh;

    // ===== GET/SET =====
    public String getMaTienTrinh() {
        return maTienTrinh;
    }

    public void setMaTienTrinh(String maTienTrinh) {
        this.maTienTrinh = maTienTrinh;
    }

    public NguoiDung getNguoiDung() {
        return nguoiDung;
    }

    public void setNguoiDung(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
    }

    public HoatDongHocTap getHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(HoatDongHocTap hoatDong) {
        this.hoatDong = hoatDong;
    }

    public LocalDateTime getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDateTime getNgayHoanThanh() {
        return ngayHoanThanh;
    }

    public void setNgayHoanThanh(LocalDateTime ngayHoanThanh) {
        this.ngayHoanThanh = ngayHoanThanh;
    }

    public int getSoCauDung() {
        return soCauDung;
    }

    public void setSoCauDung(int soCauDung) {
        this.soCauDung = soCauDung;
    }

    public int getSoCauDaLam() {
        return soCauDaLam;
    }

    public void setSoCauDaLam(int soCauDaLam) {
        this.soCauDaLam = soCauDaLam;
    }

    public int getDiemDatDuoc() {
        return diemDatDuoc;
    }

    public void setDiemDatDuoc(int diemDatDuoc) {
        this.diemDatDuoc = diemDatDuoc;
    }

    public boolean isDaHoanThanh() {
        return daHoanThanh;
    }

    public void setDaHoanThanh(boolean daHoanThanh) {
        this.daHoanThanh = daHoanThanh;
    }
}
