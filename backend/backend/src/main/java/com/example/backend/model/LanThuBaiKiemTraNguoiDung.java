package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LanThuBaiKiemTraNguoiDung")
public class LanThuBaiKiemTraNguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maLanThu;

    @Column(name = "MaNguoiDung", nullable = false)
    private Integer maNguoiDung;

    @Column(name = "MaBaiKiemTra", nullable = false)
    private Integer maBaiKiemTra;

    @Column(name = "Diem", nullable = false)
    private Integer diem;

    @Column(name = "DiemToiDa", nullable = false)
    private Integer diemToiDa;

    @Column(name = "NgayHoanThanh", nullable = false)
    private LocalDateTime ngayHoanThanh = LocalDateTime.now();

    // Getter và Setter
    public Integer getMaLanThu() {
        return maLanThu;
    }

    public void setMaLanThu(Integer maLanThu) {
        this.maLanThu = maLanThu;
    }

    public Integer getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(Integer maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public Integer getMaBaiKiemTra() {
        return maBaiKiemTra;
    }

    public void setMaBaiKiemTra(Integer maBaiKiemTra) {
        this.maBaiKiemTra = maBaiKiemTra;
    }

    public Integer getDiem() {
        return diem;
    }

    public void setDiem(Integer diem) {
        this.diem = diem;
    }

    public Integer getDiemToiDa() {
        return diemToiDa;
    }

    public void setDiemToiDa(Integer diemToiDa) {
        this.diemToiDa = diemToiDa;
    }

    public LocalDateTime getNgayHoanThanh() {
        return ngayHoanThanh;
    }

    public void setNgayHoanThanh(LocalDateTime ngayHoanThanh) {
        this.ngayHoanThanh = ngayHoanThanh;
    }
}