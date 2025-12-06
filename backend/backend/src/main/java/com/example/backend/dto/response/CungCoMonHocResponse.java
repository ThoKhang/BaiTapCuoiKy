package com.example.backend.dto.response;

public class CungCoMonHocResponse {

    private String maHoatDong;
    private String tieuDe;
    private String moTa;
    private int tongDiemToiDa;

    public CungCoMonHocResponse(String maHoatDong, String tieuDe, String moTa, int tongDiemToiDa) {
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
