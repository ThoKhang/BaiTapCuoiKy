package com.example.apphoctapchotre.DATA.model;

public class DapAn {
    private String maDapAn;
    private String noiDungDapAn;
    private boolean isSelected; // Để tracking đáp án được chọn
    private boolean laDapAnDung; // THÊM từ API
    private String giaiThich;    // THÊM từ API

    // Constructor
    public DapAn() {
    }

    public DapAn(String maDapAn, String noiDungDapAn, boolean laDapAnDung, String giaiThich) {
        this.maDapAn = maDapAn;
        this.noiDungDapAn = noiDungDapAn;
        this.laDapAnDung = laDapAnDung;
        this.giaiThich = giaiThich;
        this.isSelected = false;
    }

    // Getters
    public String getMaDapAn() {
        return maDapAn;
    }

    public String getNoiDungDapAn() {
        return noiDungDapAn;
    }

    public boolean isSelected() {
        return isSelected;
    }

    // THÊM GETTER CHO laDapAnDung
    public boolean isLaDapAnDung() {
        return laDapAnDung;
    }

    // THÊM GETTER CHO giaiThich
    public String getGiaiThich() {
        return giaiThich;
    }

    // Setters
    public void setMaDapAn(String maDapAn) {
        this.maDapAn = maDapAn;
    }

    public void setNoiDungDapAn(String noiDungDapAn) {
        this.noiDungDapAn = noiDungDapAn;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    // THÊM SETTER CHO laDapAnDung
    public void setLaDapAnDung(boolean laDapAnDung) {
        this.laDapAnDung = laDapAnDung;
    }

    // THÊM SETTER CHO giaiThich
    public void setGiaiThich(String giaiThich) {
        this.giaiThich = giaiThich;
    }
}