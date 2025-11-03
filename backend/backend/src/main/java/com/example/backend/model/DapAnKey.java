package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DapAnKey implements Serializable {
    @Column(name = "MaLanThu", nullable = false)
    private Integer maLanThu;

    @Column(name = "MaCauHoi", nullable = false)
    private Integer maCauHoi;

    // Getter và Setter
    public Integer getMaLanThu() {
        return maLanThu;
    }

    public void setMaLanThu(Integer maLanThu) {
        this.maLanThu = maLanThu;
    }

    public Integer getMaCauHoi() {
        return maCauHoi;
    }

    public void setMaCauHoi(Integer maCauHoi) {
        this.maCauHoi = maCauHoi;
    }
}