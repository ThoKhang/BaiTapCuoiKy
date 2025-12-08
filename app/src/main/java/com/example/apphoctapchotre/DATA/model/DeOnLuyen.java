package com.example.apphoctapchotre.DATA.model;

import java.util.List;

public class DeOnLuyen {
    private String maHoatDong;
    private String tieuDe;
    private List<CauHoiResponse> danhSachCauHoi;

    public String getMaHoatDong() {
        return maHoatDong;
    }

    public void setMaHoatDong(String maHoatDong) {
        this.maHoatDong = maHoatDong;
    }

    public List<CauHoiResponse> getDanhSachCauHoi() {
        return danhSachCauHoi;
    }

    public void setDanhSachCauHoi(List<CauHoiResponse> danhSachCauHoi) {
        this.danhSachCauHoi = danhSachCauHoi;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }
}
