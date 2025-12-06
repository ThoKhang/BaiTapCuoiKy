package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class HoatDongCauHoiKey implements Serializable {

    @Column(name = "MaHoatDong", length = 5)
    private String maHoatDong;

    @Column(name = "MaCauHoi", length = 5)
    private String maCauHoi;

    public HoatDongCauHoiKey() {
    }

    public HoatDongCauHoiKey(String maHoatDong, String maCauHoi) {
        this.maHoatDong = maHoatDong;
        this.maCauHoi = maCauHoi;
    }

    public String getMaHoatDong() {
        return maHoatDong;
    }

    public void setMaHoatDong(String maHoatDong) {
        this.maHoatDong = maHoatDong;
    }

    public String getMaCauHoi() {
        return maCauHoi;
    }

    public void setMaCauHoi(String maCauHoi) {
        this.maCauHoi = maCauHoi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoatDongCauHoiKey)) return false;
        HoatDongCauHoiKey that = (HoatDongCauHoiKey) o;
        return Objects.equals(maHoatDong, that.maHoatDong) &&
               Objects.equals(maCauHoi, that.maCauHoi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHoatDong, maCauHoi);
    }
}
