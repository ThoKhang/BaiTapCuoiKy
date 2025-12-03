/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "HoatDongHocTap")
public class HoatDongHocTap {
    @Id
    @Column(name = "MaHoatDong", length = 5)
    private String maHoatDong;
    @ManyToOne
    @JoinColumn(name = "MaMonHoc")
    private MonHoc monHoc;
    @ManyToOne
    @JoinColumn(name = "MaLoai")
    private LoaiHoatDong loaiHoatDong;
    @Column(name = "TieuDe", nullable = false, length = 100)
    private String tieuDe;
    @Column(name = "MoTa", length = 300)
    private String moTa;
    @Column(name = "TongDiemToiDa", nullable = false)
    private Integer tongDiemToiDa;
    @OneToMany(mappedBy = "hoatDong", fetch = FetchType.LAZY)
    private List<HoatDongCauHoi> danhSachCauHoi;

    public HoatDongHocTap() {
    }

    public HoatDongHocTap(String maHoatDong, MonHoc monHoc, LoaiHoatDong loaiHoatDong, String tieuDe, String moTa, Integer tongDiemToiDa, List<HoatDongCauHoi> danhSachCauHoi) {
        this.maHoatDong = maHoatDong;
        this.monHoc = monHoc;
        this.loaiHoatDong = loaiHoatDong;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.tongDiemToiDa = tongDiemToiDa;
        this.danhSachCauHoi = danhSachCauHoi;
    }

    public String getMaHoatDong() {
        return maHoatDong;
    }

    public void setMaHoatDong(String maHoatDong) {
        this.maHoatDong = maHoatDong;
    }

    public MonHoc getMonHoc() {
        return monHoc;
    }

    public void setMonHoc(MonHoc monHoc) {
        this.monHoc = monHoc;
    }

    public LoaiHoatDong getLoaiHoatDong() {
        return loaiHoatDong;
    }

    public void setLoaiHoatDong(LoaiHoatDong loaiHoatDong) {
        this.loaiHoatDong = loaiHoatDong;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Integer getTongDiemToiDa() {
        return tongDiemToiDa;
    }

    public void setTongDiemToiDa(Integer tongDiemToiDa) {
        this.tongDiemToiDa = tongDiemToiDa;
    }

    public List<HoatDongCauHoi> getDanhSachCauHoi() {
        return danhSachCauHoi;
    }

    public void setDanhSachCauHoi(List<HoatDongCauHoi> danhSachCauHoi) {
        this.danhSachCauHoi = danhSachCauHoi;
    }
    
}
