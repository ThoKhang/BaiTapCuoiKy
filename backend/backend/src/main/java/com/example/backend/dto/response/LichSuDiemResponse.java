package com.example.backend.dto.response;

import java.util.List;

public class LichSuDiemResponse {

    private int tongDiem;              // Tổng điểm của người dùng (NguoiDung.TongDiem)
    private int diemKiemTra;           // Tổng điểm loại "Kiểm tra"
    private int diemHoatDong;          // Phần còn lại (hoạt động khác)
    private List<LichSuDiemItem> danhSachChiTiet;

    public int getTongDiem() {
        return tongDiem;
    }

    public void setTongDiem(int tongDiem) {
        this.tongDiem = tongDiem;
    }

    public int getDiemKiemTra() {
        return diemKiemTra;
    }

    public void setDiemKiemTra(int diemKiemTra) {
        this.diemKiemTra = diemKiemTra;
    }

    public int getDiemHoatDong() {
        return diemHoatDong;
    }

    public void setDiemHoatDong(int diemHoatDong) {
        this.diemHoatDong = diemHoatDong;
    }

    public List<LichSuDiemItem> getDanhSachChiTiet() {
        return danhSachChiTiet;
    }

    public void setDanhSachChiTiet(List<LichSuDiemItem> danhSachChiTiet) {
        this.danhSachChiTiet = danhSachChiTiet;
    }
}
