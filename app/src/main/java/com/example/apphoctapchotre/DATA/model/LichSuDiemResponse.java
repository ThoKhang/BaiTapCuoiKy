package com.example.apphoctapchotre.DATA.model;

import java.util.List;

public class LichSuDiemResponse {

    private int tongDiem;
    private int diemKiemTra;
    private int diemHoatDong;
    private List<DiemChiTiet> danhSachChiTiet;

    public int getTongDiem() {
        return tongDiem;
    }

    public int getDiemKiemTra() {
        return diemKiemTra;
    }

    public int getDiemHoatDong() {
        return diemHoatDong;
    }

    public List<DiemChiTiet> getDanhSachChiTiet() {
        return danhSachChiTiet;
    }
}
