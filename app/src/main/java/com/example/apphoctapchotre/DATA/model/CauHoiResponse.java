package com.example.apphoctapchotre.DATA.model;

import java.util.List;

public class CauHoiResponse {

    private String maCauHoi;
    private String noiDungCauHoi;
    private int diemToiDa;
    private int thuTu;
    private List<DapAnResponse> dapAn;

    // Constructor
    public CauHoiResponse(String maCauHoi, String noiDungCauHoi, int diemToiDa,
                          int thuTu, List<DapAnResponse> dapAn) {
        this.maCauHoi = maCauHoi;
        this.noiDungCauHoi = noiDungCauHoi;
        this.diemToiDa = diemToiDa;
        this.thuTu = thuTu;
        this.dapAn = dapAn;
    }

    public CauHoiResponse() {
    }

    // Getters
    public String getMaCauHoi() { return maCauHoi; }
    public String getNoiDungCauHoi() { return noiDungCauHoi; }
    public int getDiemToiDa() { return diemToiDa; }
    public int getThuTu() { return thuTu; }
    public List<DapAnResponse> getDapAn() { return dapAn; }

    // Setters
    public void setMaCauHoi(String maCauHoi) { this.maCauHoi = maCauHoi; }
    public void setNoiDungCauHoi(String noiDungCauHoi) { this.noiDungCauHoi = noiDungCauHoi; }
    public void setDiemToiDa(int diemToiDa) { this.diemToiDa = diemToiDa; }
    public void setThuTu(int thuTu) { this.thuTu = thuTu; }
    public void setDapAn(List<DapAnResponse> dapAn) { this.dapAn = dapAn; }

    // ============================================
    // CLASS TRONG: DapAnResponse
    // ============================================
    public static class DapAnResponse {
        private String maDapAn;
        private String noiDungDapAn;

        public DapAnResponse(String maDapAn, String noiDungDapAn) {
            this.maDapAn = maDapAn;
            this.noiDungDapAn = noiDungDapAn;
        }

        public DapAnResponse() {
        }

        public String getMaDapAn() { return maDapAn; }
        public String getNoiDungDapAn() { return noiDungDapAn; }

        public void setMaDapAn(String maDapAn) { this.maDapAn = maDapAn; }
        public void setNoiDungDapAn(String noiDungDapAn) { this.noiDungDapAn = noiDungDapAn; }
    }
}