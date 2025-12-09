package com.example.apphoctapchotre.DATA.model;

import java.util.List;

public class DeOnLuyen {
    private String maHoatDong;
    private String tieuDe;
    private List<CauHoi> danhSachCauHoi;

    public String getMaHoatDong() {
        return maHoatDong;
    }

    public void setMaHoatDong(String maHoatDong) {
        this.maHoatDong = maHoatDong;
    }

    public List<CauHoi> getDanhSachCauHoi() {
        return danhSachCauHoi;
    }

    public void setDanhSachCauHoi(List<CauHoi> danhSachCauHoi) {
        this.danhSachCauHoi = danhSachCauHoi;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }
}
