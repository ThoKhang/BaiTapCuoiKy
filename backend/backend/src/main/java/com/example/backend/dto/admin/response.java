package com.example.backend.dto.admin;

public class response {
    public interface ThongKeTheoLoaiDTO {
        String getTenLoai();
        Integer getSoLuot();
    }
    public interface HoatDongTheoNgayDTO {
        String getNgay();     // yyyy-MM-dd
        Integer getSoLuot();
    }
    public interface ThongKeTheoMonDTO {
        String getTenMonHoc();
        Integer getSoLuot();
    }
    public interface AnalyticsOverviewDTO {
        Integer getTongLuotThamGia();      // total attempts
        Integer getSoNguoiDungThamGia();   // unique students
        Integer getSoHoanThanh();          // completed count
        Double getTyLeHoanThanh();         // %
        Double getThoiGianTBPhut();        // avg minutes (completed only)
        Integer getTongDiem();             // sum score
    }
    public interface ActivityPopularityDTO {
        String getMaHoatDong();
        String getTieuDe();
        String getTenLoai();
        String getTenMonHoc();
        Integer getSoLuot();
    }
    public interface ActivityCompletionDTO {
        String getMaHoatDong();
        String getTieuDe();
        Integer getTongLuot();
        Integer getHoanThanh();
        Double getTyLeHoanThanh();
    }
    public interface ActivityAvgTimeDTO {
        String getMaHoatDong();
        String getTieuDe();
        Double getThoiGianTBPhut();
    }
    public interface SubjectPopularityDTO {
        String getMaMonHoc();
        String getTenMonHoc();
        Integer getSoLuot();
    }
    public interface TopStudentDTO {
        String getMaNguoiDung();
        String getTenDangNhap();
        Integer getSoLuot();
        Integer getSoHoanThanh();
        Integer getTongDiem();
    }

}
