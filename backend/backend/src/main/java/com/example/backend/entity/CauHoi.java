package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CauHoi")
public class CauHoi {

    @Id
    @Column(name = "MaCauHoi", length = 5)
    private String maCauHoi;

    @Column(name = "NoiDungCauHoi", length = 300, nullable = false)
    private String noiDungCauHoi;

    @Column(name = "GiaiThich", length = 300)
    private String giaiThich;

    @Column(name = "DiemToiDa", nullable = false)
    private int diemToiDa;

    // ==================== Constructor ====================
    public CauHoi() {
    }

    public CauHoi(String maCauHoi, String noiDungCauHoi, String giaiThich, int diemToiDa) {
        this.maCauHoi = maCauHoi;
        this.noiDungCauHoi = noiDungCauHoi;
        this.giaiThich = giaiThich;
        this.diemToiDa = diemToiDa;
    }

    // ==================== Getters & Setters ====================
    public String getMaCauHoi() {
        return maCauHoi;
    }

    public void setMaCauHoi(String maCauHoi) {
        this.maCauHoi = maCauHoi;
    }

    public String getNoiDungCauHoi() {
        return noiDungCauHoi;
    }

    public void setNoiDungCauHoi(String noiDungCauHoi) {
        this.noiDungCauHoi = noiDungCauHoi;
    }

    public String getGiaiThich() {
        return giaiThich;
    }

    public void setGiaiThich(String giaiThich) {
        this.giaiThich = giaiThich;
    }

    public int getDiemToiDa() {
        return diemToiDa;
    }

    public void setDiemToiDa(int diemToiDa) {
        this.diemToiDa = diemToiDa;
    }

    // ==================== toString ====================
    @Override
    public String toString() {
        return "CauHoi{" +
                "maCauHoi='" + maCauHoi + '\'' +
                ", noiDungCauHoi='" + noiDungCauHoi + '\'' +
                ", giaiThich='" + giaiThich + '\'' +
                ", diemToiDa=" + diemToiDa +
                '}';
    }
}