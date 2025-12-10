package com.example.backend.dto.response;

public class LyThuyetMonHocResponse {
    private String maHoatDong;
    private String tieuDe;
    private String moTa;
    private int tongDiemToiDa;

    public LyThuyetMonHocResponse(String maHoatDong, String tieuDe, String moTa, int tongDiemToiDa) {
        this.maHoatDong = maHoatDong;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.tongDiemToiDa = tongDiemToiDa;
    }

    public String getMaHoatDong() { return maHoatDong; }
    public String getTieuDe() { return tieuDe; }
    public String getMoTa() { return moTa; }
    public int getTongDiemToiDa() { return tongDiemToiDa; }
}