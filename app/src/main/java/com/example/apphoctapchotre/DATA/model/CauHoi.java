package com.example.apphoctapchotre.DATA.model;

import java.util.List;

public class CauHoi {
    private String maCauHoi;
    private int thuTu;
    private String noiDungCauHoi;
    private int diemToiDa;
    private List<DapAn> dapAn;

    // Constructor
    public CauHoi() {
    }

    public CauHoi(String maCauHoi, int thuTu, String noiDungCauHoi, int diemToiDa, List<DapAn> dapAn) {
        this.maCauHoi = maCauHoi;
        this.thuTu = thuTu;
        this.noiDungCauHoi = noiDungCauHoi;
        this.diemToiDa = diemToiDa;
        this.dapAn = dapAn;
    }

    // Getters
    public String getMaCauHoi() {
        return maCauHoi;
    }

    public int getThuTu() {
        return thuTu;
    }

    public String getNoiDungCauHoi() {
        return noiDungCauHoi;
    }

    public int getDiemToiDa() {
        return diemToiDa;
    }

    public List<DapAn> getDapAn() {
        return dapAn;
    }

    // Setters
    public void setMaCauHoi(String maCauHoi) {
        this.maCauHoi = maCauHoi;
    }

    public void setThuTu(int thuTu) {
        this.thuTu = thuTu;
    }

    public void setNoiDungCauHoi(String noiDungCauHoi) {
        this.noiDungCauHoi = noiDungCauHoi;
    }

    public void setDiemToiDa(int diemToiDa) {
        this.diemToiDa = diemToiDa;
    }

    public void setDapAn(List<DapAn> dapAn) {
        this.dapAn = dapAn;
    }
}