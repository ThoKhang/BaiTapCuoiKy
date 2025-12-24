package com.example.apphoctapchotre.DATA.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Media implements Serializable {

    @SerializedName("maMedia")
    private Long maMedia;

    @SerializedName("tieuDe")
    private String tieuDe;

    @SerializedName("moTa")
    private String moTa;

    @SerializedName("loaiMedia")
    private String loaiMedia;

    @SerializedName("duongDanFile")
    private String duongDanFile;

    @SerializedName("thoiLuongGiay")
    private Integer thoiLuongGiay;

    @SerializedName("ngayTao")
    private String ngayTao;

    public Long getMaMedia() { return maMedia; }
    public String getTieuDe() { return tieuDe; }
    public String getMoTa() { return moTa; }
    public String getLoaiMedia() { return loaiMedia; }
    public String getDuongDanFile() { return duongDanFile; }
    public Integer getThoiLuongGiay() { return thoiLuongGiay; }
}
