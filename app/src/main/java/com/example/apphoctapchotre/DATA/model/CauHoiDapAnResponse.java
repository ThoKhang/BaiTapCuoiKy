package com.example.apphoctapchotre.DATA.model;

public class CauHoiDapAnResponse {

    private String maCauHoi;
    private int thuTu;
    private String noiDungCauHoi;
    private String maDapAn;
    private String noiDungDapAn;
    private boolean laDapAnDung;
    private String giaiThich;
    private int diemToiDa;

    // GETTERS
    public String getMaCauHoi() {
        return maCauHoi;
    }

    public int getThuTu() {
        return thuTu;
    }

    public String getNoiDungCauHoi() {
        return noiDungCauHoi;
    }

    public String getMaDapAn() {
        return maDapAn;
    }

    public String getNoiDungDapAn() {
        return noiDungDapAn;
    }

    public boolean isLaDapAnDung() {
        return laDapAnDung;
    }

    public String getGiaiThich() {
        return giaiThich;
    }

    public int getDiemToiDa() {
        return diemToiDa;
    }

    // SETTERS
    public void setMaCauHoi(String maCauHoi) {
        this.maCauHoi = maCauHoi;
    }

    public void setThuTu(int thuTu) {
        this.thuTu = thuTu;
    }

    public void setNoiDungCauHoi(String noiDungCauHoi) {
        this.noiDungCauHoi = noiDungCauHoi;
    }

    public void setMaDapAn(String maDapAn) {
        this.maDapAn = maDapAn;
    }

    public void setNoiDungDapAn(String noiDungDapAn) {
        this.noiDungDapAn = noiDungDapAn;
    }

    public void setLaDapAnDung(boolean laDapAnDung) {
        this.laDapAnDung = laDapAnDung;
    }

    public void setGiaiThich(String giaiThich) {
        this.giaiThich = giaiThich;
    }

    public void setDiemToiDa(int diemToiDa) {
        this.diemToiDa = diemToiDa;
    }
}
