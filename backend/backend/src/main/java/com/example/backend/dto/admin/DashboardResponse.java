package com.example.backend.dto.admin;

import java.util.List;

public class DashboardResponse {

    private long tongHocSinh;
    private long tongMonHoc;
    private long tongHoatDong;
    private long tongCauHoi;
    private long tongLuotLamBai;

    private List<TopHocSinh> topHocSinhTheoDiem;
    private List<TopHocSinhTheoHoatDong> topHocSinhTheoSoHoatDong;
    private List<ThongKeMonHoc> thongKeTheoMonHoc;

    public long getTongHocSinh() {
        return tongHocSinh;
    }

    public void setTongHocSinh(long tongHocSinh) {
        this.tongHocSinh = tongHocSinh;
    }

    public long getTongMonHoc() {
        return tongMonHoc;
    }

    public void setTongMonHoc(long tongMonHoc) {
        this.tongMonHoc = tongMonHoc;
    }

    public long getTongHoatDong() {
        return tongHoatDong;
    }

    public void setTongHoatDong(long tongHoatDong) {
        this.tongHoatDong = tongHoatDong;
    }

    public long getTongCauHoi() {
        return tongCauHoi;
    }

    public void setTongCauHoi(long tongCauHoi) {
        this.tongCauHoi = tongCauHoi;
    }

    public long getTongLuotLamBai() {
        return tongLuotLamBai;
    }

    public void setTongLuotLamBai(long tongLuotLamBai) {
        this.tongLuotLamBai = tongLuotLamBai;
    }

    public List<TopHocSinh> getTopHocSinhTheoDiem() {
        return topHocSinhTheoDiem;
    }

    public void setTopHocSinhTheoDiem(List<TopHocSinh> topHocSinhTheoDiem) {
        this.topHocSinhTheoDiem = topHocSinhTheoDiem;
    }

    public List<TopHocSinhTheoHoatDong> getTopHocSinhTheoSoHoatDong() {
        return topHocSinhTheoSoHoatDong;
    }

    public void setTopHocSinhTheoSoHoatDong(List<TopHocSinhTheoHoatDong> topHocSinhTheoSoHoatDong) {
        this.topHocSinhTheoSoHoatDong = topHocSinhTheoSoHoatDong;
    }

    public List<ThongKeMonHoc> getThongKeTheoMonHoc() {
        return thongKeTheoMonHoc;
    }

    public void setThongKeTheoMonHoc(List<ThongKeMonHoc> thongKeTheoMonHoc) {
        this.thongKeTheoMonHoc = thongKeTheoMonHoc;
    }

    // ===== Class con =====
    public static class TopHocSinh {
        private String maNguoiDung;
        private String tenDangNhap;
        private int tongDiem;

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

        public int getTongDiem() {
            return tongDiem;
        }

        public void setTongDiem(int tongDiem) {
            this.tongDiem = tongDiem;
        }
    }

    public static class TopHocSinhTheoHoatDong {
        private String maNguoiDung;
        private String tenDangNhap;
        private long soHoatDongDaLam;

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

        public long getSoHoatDongDaLam() {
            return soHoatDongDaLam;
        }

        public void setSoHoatDongDaLam(long soHoatDongDaLam) {
            this.soHoatDongDaLam = soHoatDongDaLam;
        }
    }

    public static class ThongKeMonHoc {
        private String maMonHoc;
        private String tenMonHoc;
        private long soHoatDong;

        public String getMaMonHoc() {
            return maMonHoc;
        }

        public void setMaMonHoc(String maMonHoc) {
            this.maMonHoc = maMonHoc;
        }

        public String getTenMonHoc() {
            return tenMonHoc;
        }

        public void setTenMonHoc(String tenMonHoc) {
            this.tenMonHoc = tenMonHoc;
        }

        public long getSoHoatDong() {
            return soHoatDong;
        }

        public void setSoHoatDong(long soHoatDong) {
            this.soHoatDong = soHoatDong;
        }
    }
}
