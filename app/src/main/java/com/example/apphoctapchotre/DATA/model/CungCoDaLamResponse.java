package com.example.apphoctapchotre.DATA.model;

public class CungCoDaLamResponse {

    private String maHoatDong;
    private String tieuDe;
    private String moTa;
    private int tongDiemToiDa;
    private int soCauDung;
    private int soCauDaLam;
    private int diemDatDuoc;
    private boolean daHoanThanh;
    private String ngayBatDau;
    private String ngayHoanThanh;

    // Constructor
    public CungCoDaLamResponse(String maHoatDong, String tieuDe, String moTa, int tongDiemToiDa,
                               int soCauDung, int soCauDaLam, int diemDatDuoc, boolean daHoanThanh,
                               String ngayBatDau, String ngayHoanThanh) {
        this.maHoatDong = maHoatDong;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.tongDiemToiDa = tongDiemToiDa;
        this.soCauDung = soCauDung;
        this.soCauDaLam = soCauDaLam;
        this.diemDatDuoc = diemDatDuoc;
        this.daHoanThanh = daHoanThanh;
        this.ngayBatDau = ngayBatDau;
        this.ngayHoanThanh = ngayHoanThanh;
    }

    // Getters
    public String getMaHoatDong() { return maHoatDong; }
    public String getTieuDe() { return tieuDe; }
    public String getMoTa() { return moTa; }
    public int getTongDiemToiDa() { return tongDiemToiDa; }
    public int getSoCauDung() { return soCauDung; }
    public int getSoCauDaLam() { return soCauDaLam; }
    public int getDiemDatDuoc() { return diemDatDuoc; }
    public boolean isDaHoanThanh() { return daHoanThanh; }
    public String getNgayBatDau() { return ngayBatDau; }
    public String getNgayHoanThanh() { return ngayHoanThanh; }

    // Setters
    public void setMaHoatDong(String maHoatDong) { this.maHoatDong = maHoatDong; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public void setTongDiemToiDa(int tongDiemToiDa) { this.tongDiemToiDa = tongDiemToiDa; }
    public void setSoCauDung(int soCauDung) { this.soCauDung = soCauDung; }
    public void setSoCauDaLam(int soCauDaLam) { this.soCauDaLam = soCauDaLam; }
    public void setDiemDatDuoc(int diemDatDuoc) { this.diemDatDuoc = diemDatDuoc; }
    public void setDaHoanThanh(boolean daHoanThanh) { this.daHoanThanh = daHoanThanh; }
    public void setNgayBatDau(String ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public void setNgayHoanThanh(String ngayHoanThanh) { this.ngayHoanThanh = ngayHoanThanh; }
}