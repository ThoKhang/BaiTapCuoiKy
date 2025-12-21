package com.example.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class HoatDongCauHoiId implements Serializable {

    private String maHoatDong;
    private String maCauHoi;

    public HoatDongCauHoiId() {}

    public HoatDongCauHoiId(String maHoatDong, String maCauHoi) {
        this.maHoatDong = maHoatDong;
        this.maCauHoi = maCauHoi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoatDongCauHoiId)) return false;
        HoatDongCauHoiId that = (HoatDongCauHoiId) o;
        return Objects.equals(maHoatDong, that.maHoatDong)
                && Objects.equals(maCauHoi, that.maCauHoi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHoatDong, maCauHoi);
    }
}
