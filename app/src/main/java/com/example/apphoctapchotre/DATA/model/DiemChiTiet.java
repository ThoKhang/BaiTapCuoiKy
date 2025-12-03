package com.example.apphoctapchotre.DATA.model;


// Model đại diện cho mỗi mục chi tiết điểm
public class DiemChiTiet {
    private int soDiem;  // Số điểm (ví dụ: 10)
    private String thoiGian;  // Thời gian (ví dụ: "17/11/2025 15:30")
    private String thongTin;  // Thông tin cộng điểm (ví dụ: "Hoàn thành bài kiểm tra toán")

    // Constructor mặc định
    public DiemChiTiet() {}

    // Constructor đầy đủ
    public DiemChiTiet(int soDiem, String thoiGian, String thongTin) {
        this.soDiem = soDiem;
        this.thoiGian = thoiGian;
        this.thongTin = thongTin;
    }

    // Getter và Setter
    public int getSoDiem() {
        return soDiem;
    }

    public void setSoDiem(int soDiem) {
        this.soDiem = soDiem;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getThongTin() {
        return thongTin;
    }

    public void setThongTin(String thongTin) {
        this.thongTin = thongTin;
    }
}