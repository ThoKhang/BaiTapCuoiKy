package com.example.apphoctapchotre.DATA.model;

import java.io.Serializable;

public class Media implements Serializable {

    private Long maMedia;
    private String tieuDe;
    private String moTa;
    private String loaiMedia;
    private String duongDanFile;
    private Integer thoiLuongGiay;
    private String ngayTao;

    public Long getMaMedia() { return maMedia; }
    public String getTieuDe() { return tieuDe; }
    public String getMoTa() { return moTa; }
    public String getLoaiMedia() { return loaiMedia; }
    public String getDuongDanFile() { return duongDanFile; }
    public Integer getThoiLuongGiay() { return thoiLuongGiay; }
}
