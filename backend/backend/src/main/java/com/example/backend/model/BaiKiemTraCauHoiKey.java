package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BaiKiemTraCauHoiKey implements Serializable {
    @Column(name = "MaBaiKiemTra", nullable = false)
    private Integer maBaiKiemTra;

    @Column(name = "MaCauHoi", nullable = false)
    private Integer maCauHoi;

    // Getter và Setter
    public Integer getMaBaiKiemTra() {
        return maBaiKiemTra;
    }

    public void setMaBaiKiemTra(Integer maBaiKiemTra) {
        this.maBaiKiemTra = maBaiKiemTra;
    }

    public Integer getMaCauHoi() {
        return maCauHoi;
    }

    public void setMaCauHoi(Integer maCauHoi) {
        this.maCauHoi = maCauHoi;
    }
}