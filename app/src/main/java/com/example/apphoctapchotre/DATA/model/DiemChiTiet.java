package com.example.apphoctapchotre.DATA.model;

import com.google.gson.annotations.SerializedName;

public class DiemChiTiet {

    @SerializedName("soDiem")
    private int soDiem;

    @SerializedName("thoiGian")
    private String thoiGian;

    @SerializedName("thongTin")
    private String thongTin;

    public DiemChiTiet() {}

    public DiemChiTiet(int soDiem, String thoiGian, String thongTin) {
        this.soDiem = soDiem;
        this.thoiGian = thoiGian;
        this.thongTin = thongTin;
    }

    public int getSoDiem() { return soDiem; }
    public String getThoiGian() { return thoiGian; }
    public String getThongTin() { return thongTin; }

    public void setSoDiem(int soDiem) { this.soDiem = soDiem; }
    public void setThoiGian(String thoiGian) { this.thoiGian = thoiGian; }
    public void setThongTin(String thongTin) { this.thongTin = thongTin; }
}
