/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.example.backend.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "LanThuBaiKiemTraNguoiDung")
public class LanThuBaiKiemTraNguoiDungEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLanThu")
    private Integer maLanThu;
    @Column(name = "MaNguoiDung")
    private Integer maNguoiDung;
    @Column(name = "Diem")
    private Integer diem;
    @Column(name = "DiemToiDa")
    private Integer diemToiDa;
    @Column(name = "NgayHoanThanh")
    private LocalDateTime ngayHoanThanh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBaiKiemTra", insertable = false, updatable = false)
    private BaiKiemTraEntity baiKiemTra;

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

    public BaiKiemTraEntity getBaiKiemTra() {
        return baiKiemTra;
    }

    public void setBaiKiemTra(BaiKiemTraEntity baiKiemTra) {
        this.baiKiemTra = baiKiemTra;
    }

    
}
