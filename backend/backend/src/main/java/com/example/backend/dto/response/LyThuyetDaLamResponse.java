package com.example.backend.dto.response;

import java.time.LocalDateTime;

public class LyThuyetDaLamResponse {
    private String maHoatDong;
    private String tieuDe;
    private String moTa;
    private int tongDiemToiDa;
    private int diemDatDuoc;
    private boolean daHoanThanh;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayHoanThanh;

    public LyThuyetDaLamResponse(String maHoatDong, String tieuDe, String moTa, int tongDiemToiDa,
                                 int diemDatDuoc, boolean daHoanThanh,
                                 LocalDateTime ngayBatDau, LocalDateTime ngayHoanThanh) {
        this.maHoatDong = maHoatDong;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.tongDiemToiDa = tongDiemToiDa;
        this.diemDatDuoc = diemDatDuoc;
        this.daHoanThanh = daHoanThanh;
        this.ngayBatDau = ngayBatDau;
        this.ngayHoanThanh = ngayHoanThanh;
    }

    public String getMaHoatDong() { return maHoatDong; }
    public String getTieuDe() { return tieuDe; }
    public String getMoTa() { return moTa; }
    public int getTongDiemToiDa() { return tongDiemToiDa; }
    public int getDiemDatDuoc() { return diemDatDuoc; }
    public boolean isDaHoanThanh() { return daHoanThanh; }
    public LocalDateTime getNgayBatDau() { return ngayBatDau; }
    public LocalDateTime getNgayHoanThanh() { return ngayHoanThanh; }

    public void setMaHoatDong(String maHoatDong) { this.maHoatDong = maHoatDong; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public void setTongDiemToiDa(int tongDiemToiDa) { this.tongDiemToiDa = tongDiemToiDa; }
    public void setDiemDatDuoc(int diemDatDuoc) { this.diemDatDuoc = diemDatDuoc; }
    public void setDaHoanThanh(boolean daHoanThanh) { this.daHoanThanh = daHoanThanh; }
    public void setNgayBatDau(LocalDateTime ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public void setNgayHoanThanh(LocalDateTime ngayHoanThanh) { this.ngayHoanThanh = ngayHoanThanh; }
}