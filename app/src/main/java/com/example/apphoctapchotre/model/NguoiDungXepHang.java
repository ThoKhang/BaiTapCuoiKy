package com.example.apphoctapchotre.model;

import com.google.gson.annotations.SerializedName;

public class NguoiDungXepHang {

    @SerializedName("tenDangNhap")
    private String tenDangNhap;

    @SerializedName("tongDiem")
    private int tongDiem;

    @SerializedName("hang")
    private int hang;

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public int getTongDiem() {
        return tongDiem;
    }

    public void setTongDiem(int tongDiem) {
        this.tongDiem = tongDiem;
    }

    public int getHang() {
        return hang;
    }

    public void setHang(int hang) {
        this.hang = hang;
    }
}
