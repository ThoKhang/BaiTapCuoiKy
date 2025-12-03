/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "TienTrinhHocTap")
public class TienTrinhHocTap {

    @Id
    @Column(name = "MaTienTrinh", length = 5)
    private String maTienTrinh;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "MaHoatDong")
    private HoatDongHocTap hoatDong;

    @Column(name = "NgayBatDau")
    private java.time.LocalDateTime ngayBatDau;

    @Column(name = "NgayHoanThanh")
    private java.time.LocalDateTime ngayHoanThanh;

    @Column(name = "SoCauDung")
    private Integer soCauDung;

    @Column(name = "SoCauDaLam")
    private Integer soCauDaLam;

    @Column(name = "DiemDatDuoc")
    private Integer diemDatDuoc;

    @Column(name = "DaHoanThanh")
    private Boolean daHoanThanh;

    public TienTrinhHocTap() {
    }

    public TienTrinhHocTap(String maTienTrinh, NguoiDung nguoiDung, HoatDongHocTap hoatDong, LocalDateTime ngayBatDau, LocalDateTime ngayHoanThanh, Integer soCauDung, Integer soCauDaLam, Integer diemDatDuoc, Boolean daHoanThanh) {
        this.maTienTrinh = maTienTrinh;
        this.nguoiDung = nguoiDung;
        this.hoatDong = hoatDong;
        this.ngayBatDau = ngayBatDau;
        this.ngayHoanThanh = ngayHoanThanh;
        this.soCauDung = soCauDung;
        this.soCauDaLam = soCauDaLam;
        this.diemDatDuoc = diemDatDuoc;
        this.daHoanThanh = daHoanThanh;
    }

    public String getMaTienTrinh() {
        return maTienTrinh;
    }

    public void setMaTienTrinh(String maTienTrinh) {
        this.maTienTrinh = maTienTrinh;
    }

    public NguoiDung getNguoiDung() {
        return nguoiDung;
    }

    public void setNguoiDung(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
    }

    public HoatDongHocTap getHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(HoatDongHocTap hoatDong) {
        this.hoatDong = hoatDong;
    }

    public LocalDateTime getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDateTime getNgayHoanThanh() {
        return ngayHoanThanh;
    }

    public void setNgayHoanThanh(LocalDateTime ngayHoanThanh) {
        this.ngayHoanThanh = ngayHoanThanh;
    }

    public Integer getSoCauDung() {
        return soCauDung;
    }

    public void setSoCauDung(Integer soCauDung) {
        this.soCauDung = soCauDung;
    }

    public Integer getSoCauDaLam() {
        return soCauDaLam;
    }

    public void setSoCauDaLam(Integer soCauDaLam) {
        this.soCauDaLam = soCauDaLam;
    }

    public Integer getDiemDatDuoc() {
        return diemDatDuoc;
    }

    public void setDiemDatDuoc(Integer diemDatDuoc) {
        this.diemDatDuoc = diemDatDuoc;
    }

    public Boolean getDaHoanThanh() {
        return daHoanThanh;
    }

    public void setDaHoanThanh(Boolean daHoanThanh) {
        this.daHoanThanh = daHoanThanh;
    }

}

