package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DapAn")
public class DapAn {

    @Id
    @Column(name = "MaDapAn", length = 6)
    private String maDapAn;

    @Column(name = "MaCauHoi", length = 5, nullable = false)
    private String maCauHoi;

    @Column(name = "NoiDungDapAn", length = 100, nullable = false)
    private String noiDungDapAn;

    @Column(name = "LaDapAnDung", nullable = false)
    private Boolean laDapAnDung;

    // ==================== Constructor ====================
    public DapAn() {
    }

    public DapAn(String maDapAn, String maCauHoi, String noiDungDapAn, Boolean laDapAnDung) {
        this.maDapAn = maDapAn;
        this.maCauHoi = maCauHoi;
        this.noiDungDapAn = noiDungDapAn;
        this.laDapAnDung = laDapAnDung;
    }

    // ==================== Getters & Setters ====================
    public String getMaDapAn() {
        return maDapAn;
    }

    public void setMaDapAn(String maDapAn) {
        this.maDapAn = maDapAn;
    }

    public String getMaCauHoi() {
        return maCauHoi;
    }

    public void setMaCauHoi(String maCauHoi) {
        this.maCauHoi = maCauHoi;
    }

    public String getNoiDungDapAn() {
        return noiDungDapAn;
    }

    public void setNoiDungDapAn(String noiDungDapAn) {
        this.noiDungDapAn = noiDungDapAn;
    }

    public Boolean getLaDapAnDung() {
        return laDapAnDung;
    }

    public void setLaDapAnDung(Boolean laDapAnDung) {
        this.laDapAnDung = laDapAnDung;
    }

    // ==================== toString ====================
    @Override
    public String toString() {
        return "DapAn{" +
                "maDapAn='" + maDapAn + '\'' +
                ", maCauHoi='" + maCauHoi + '\'' +
                ", noiDungDapAn='" + noiDungDapAn + '\'' +
                ", laDapAnDung=" + laDapAnDung +
                '}';
    }
}