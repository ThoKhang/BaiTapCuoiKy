package com.example.backend.dto.response;

public class NguoiDungXepHangResponse {

    private String tenDangNhap;
    private Integer tongDiem;
    private Integer hang;

    public NguoiDungXepHangResponse() {
    }

    public NguoiDungXepHangResponse(String tenDangNhap, Integer tongDiem, Integer hang) {
        this.tenDangNhap = tenDangNhap;
        this.tongDiem = tongDiem;
        this.hang = hang;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public Integer getTongDiem() {
        return tongDiem;
    }

    public void setTongDiem(Integer tongDiem) {
        this.tongDiem = tongDiem;
    }

    public Integer getHang() {
        return hang;
    }

    public void setHang(Integer hang) {
        this.hang = hang;
    }
}
