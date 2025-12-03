package com.example.apphoctapchotre.DATA.model;

public class KhoaHocGioiThieu {

    private String tenKhoaHoc;
    private float diemDanhGia;
    private int hinhAnh;

    public KhoaHocGioiThieu(String tenKhoaHoc, float diemDanhGia, int hinhAnh) {
        this.tenKhoaHoc = tenKhoaHoc;
        this.diemDanhGia = diemDanhGia;
        this.hinhAnh = hinhAnh;
    }

    public String getTenKhoaHoc() {
        return tenKhoaHoc;
    }

    public float getDiemDanhGia() {
        return diemDanhGia;
    }

    public int getHinhAnh() {
        return hinhAnh;
    }
}