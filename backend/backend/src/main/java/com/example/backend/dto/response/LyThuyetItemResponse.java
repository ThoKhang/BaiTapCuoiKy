package com.example.backend.dto.response;

public class LyThuyetItemResponse {
    private String maHoatDong;
    private String tieuDe;
    private String moTa;
    private int diem;
    private boolean daHoanThanh;

    public LyThuyetItemResponse(String maHoatDong, String tieuDe, String moTa, int diem, boolean daHoanThanh) {
        this.maHoatDong = maHoatDong;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.diem = diem;
        this.daHoanThanh = daHoanThanh;
    }

    // Getters
    public String getMaHoatDong() { return maHoatDong; }
    public String getTieuDe() { return tieuDe; }
    public String getMoTa() { return moTa; }
    public int getDiem() { return diem; }
    public boolean isDaHoanThanh() { return daHoanThanh; }
}