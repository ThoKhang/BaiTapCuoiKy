package com.example.backend.dto.response;

import java.time.LocalDateTime;

public class NguoiDungResponse {

    private String maNguoiDung;
    private String tenDangNhap;
    private String email;
    private Integer tongDiem;
    private LocalDateTime ngayTao;

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTongDiem() {
        return tongDiem;
    }

    public void setTongDiem(Integer tongDiem) {
        this.tongDiem = tongDiem;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}
