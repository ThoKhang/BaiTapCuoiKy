package com.example.apphoctapchotre.DATA.model;

public class
XepHangItem {

    // Thuộc tính tiếng Việt
    private String tenNguoiChoi;
    private int tongDiem;

    // Constructor
    public XepHangItem(String tenNguoiChoi, int tongDiem) {
        this.tenNguoiChoi = tenNguoiChoi;
        this.tongDiem = tongDiem;
    }

    public String getTenNguoiChoi() {
        return tenNguoiChoi;
    }

    public void setTenNguoiChoi(String tenNguoiChoi) {
        this.tenNguoiChoi = tenNguoiChoi;
    }

    public int getTongDiem() {
        return tongDiem;
    }

    public void setTongDiem(int tongDiem) {
        this.tongDiem = tongDiem;
    }

    // Adapter hiện đang dùng getUsername() và getScore()

    public String getUsername() {
        return tenNguoiChoi;
    }

    public int getScore() {
        return tongDiem;
    }
}
