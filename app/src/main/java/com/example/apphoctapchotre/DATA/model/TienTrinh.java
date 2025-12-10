package com.example.apphoctapchotre.DATA.model;

import java.time.LocalDateTime;

public class TienTrinh {
    private String maTienTrinh;
    private String maNguoiDung;
    private String email;
    private String maHoatDong;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayHoanThanh;
    private int soCauDung;
    private int soCauDaLam;
    private int diemDatDuoc;
    private int daHoanThanh;
    private String tieuDe;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaTienTrinh() {
        return maTienTrinh;
    }

    public void setMaTienTrinh(String maTienTrinh) {
        this.maTienTrinh = maTienTrinh;
    }

    public int getDaHoanThanh() {
        return daHoanThanh;
    }

    public void setDaHoanThanh(int daHoanThanh) {
        this.daHoanThanh = daHoanThanh;
    }

    public int getDiemDatDuoc() {
        return diemDatDuoc;
    }

    public void setDiemDatDuoc(int diemDatDuoc) {
        this.diemDatDuoc = diemDatDuoc;
    }

    public int getSoCauDaLam() {
        return soCauDaLam;
    }

    public void setSoCauDaLam(int soCauDaLam) {
        this.soCauDaLam = soCauDaLam;
    }

    public int getSoCauDung() {
        return soCauDung;
    }

    public void setSoCauDung(int soCauDung) {
        this.soCauDung = soCauDung;
    }

    public LocalDateTime getNgayHoanThanh() {
        return ngayHoanThanh;
    }

    public void setNgayHoanThanh(LocalDateTime ngayHoanThanh) {
        this.ngayHoanThanh = ngayHoanThanh;
    }

    public LocalDateTime getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getMaHoatDong() {
        return maHoatDong;
    }

    public void setMaHoatDong(String maHoatDong) {
        this.maHoatDong = maHoatDong;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }
}
