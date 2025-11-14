package com.example.apphoctapchotre.model;

import com.google.gson.annotations.SerializedName;

public class CauHoiResponse {

    @SerializedName("MaCauHoi")
    private int maCauHoi;

    @SerializedName("CauHoi")
    private String cauHoi;

    @SerializedName("MaLuaChon")
    private int maLuaChon;

    @SerializedName("NhanLuaChon")
    private String nhanLuaChon;

    @SerializedName("DapAn")
    private String dapAn;

    @SerializedName("LaDung")
    private boolean laDung;

    // Getter / Setter
    public int getMaCauHoi() { return maCauHoi; }
    public void setMaCauHoi(int maCauHoi) { this.maCauHoi = maCauHoi; }

    public String getCauHoi() { return cauHoi; }
    public void setCauHoi(String cauHoi) { this.cauHoi = cauHoi; }

    public int getMaLuaChon() { return maLuaChon; }
    public void setMaLuaChon(int maLuaChon) { this.maLuaChon = maLuaChon; }

    public String getNhanLuaChon() { return nhanLuaChon; }
    public void setNhanLuaChon(String nhanLuaChon) { this.nhanLuaChon = nhanLuaChon; }

    public String getDapAn() { return dapAn; }
    public void setDapAn(String dapAn) { this.dapAn = dapAn; }

    public boolean isLaDung() { return laDung; }
    public void setLaDung(boolean laDung) { this.laDung = laDung; }
}
