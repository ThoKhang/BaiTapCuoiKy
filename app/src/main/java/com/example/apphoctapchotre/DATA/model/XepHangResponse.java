package com.example.apphoctapchotre.DATA.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class XepHangResponse {

    @SerializedName("tongSoNguoiChoi")
    private long tongSoNguoiChoi;

    @SerializedName("nguoiDungHienTai")
    private NguoiDungXepHang nguoiDungHienTai;

    @SerializedName("topNguoiDung")
    private List<NguoiDungXepHang> topNguoiDung;

    public long getTongSoNguoiChoi() {
        return tongSoNguoiChoi;
    }

    public void setTongSoNguoiChoi(long tongSoNguoiChoi) {
        this.tongSoNguoiChoi = tongSoNguoiChoi;
    }

    public NguoiDungXepHang getNguoiDungHienTai() {
        return nguoiDungHienTai;
    }

    public void setNguoiDungHienTai(NguoiDungXepHang nguoiDungHienTai) {
        this.nguoiDungHienTai = nguoiDungHienTai;
    }

    public List<NguoiDungXepHang> getTopNguoiDung() {
        return topNguoiDung;
    }

    public void setTopNguoiDung(List<NguoiDungXepHang> topNguoiDung) {
        this.topNguoiDung = topNguoiDung;
    }
}
