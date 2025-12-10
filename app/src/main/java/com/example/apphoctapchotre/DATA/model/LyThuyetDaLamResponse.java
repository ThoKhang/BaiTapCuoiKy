package com.example.apphoctapchotre.DATA.model;

public class LyThuyetDaLamResponse {

    private String maHoatDong;
    private String tieuDe;
    private String moTa;
    private int tongDiemToiDa;
    private boolean daHoanThanh;

    public LyThuyetDaLamResponse(String maHoatDong, String tieuDe, String moTa, int tongDiemToiDa, boolean daHoanThanh) {
        this.maHoatDong = maHoatDong;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.tongDiemToiDa = tongDiemToiDa;
        this.daHoanThanh = daHoanThanh;
    }

    public String getMaHoatDong() { return maHoatDong; }
    public String getTieuDe() { return tieuDe; }
    public String getMoTa() { return moTa; }
    public int getTongDiemToiDa() { return tongDiemToiDa; }
    public boolean isDaHoanThanh() { return daHoanThanh; }

    public void setMaHoatDong(String maHoatDong) { this.maHoatDong = maHoatDong; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public void setTongDiemToiDa(int tongDiemToiDa) { this.tongDiemToiDa = tongDiemToiDa; }
    public void setDaHoanThanh(boolean daHoanThanh) { this.daHoanThanh = daHoanThanh; }
}