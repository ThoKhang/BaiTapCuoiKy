package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TienTrinhHocTap")
public class TienTrinhHocTap {

    @Id
    @Column(name = "MaTienTrinh")
    private String maTienTrinh;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "MaHoatDong")
    private HoatDongHocTap hoatDong;

    @Column(name = "DaHoanThanh")
    private Boolean daHoanThanh;

    @Column(name = "DiemDatDuoc")
    private Integer diemDatDuoc;

    @Column(name = "NgayHoanThanh")
    private LocalDateTime ngayHoanThanh;

    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;

    public TienTrinhHocTap() {}

    // Getters & Setters
    public String getMaTienTrinh() { return maTienTrinh; }
    public void setMaTienTrinh(String maTienTrinh) { this.maTienTrinh = maTienTrinh; }

    public NguoiDung getNguoiDung() { return nguoiDung; }
    public void setNguoiDung(NguoiDung nguoiDung) { this.nguoiDung = nguoiDung; }

    public HoatDongHocTap getHoatDong() { return hoatDong; }
    public void setHoatDong(HoatDongHocTap hoatDong) { this.hoatDong = hoatDong; }

    public Boolean getDaHoanThanh() { return daHoanThanh; }
    public void setDaHoanThanh(Boolean daHoanThanh) { this.daHoanThanh = daHoanThanh; }

    public Integer getDiemDatDuoc() { return diemDatDuoc; }
    public void setDiemDatDuoc(Integer diemDatDuoc) { this.diemDatDuoc = diemDatDuoc; }

    public LocalDateTime getNgayHoanThanh() { return ngayHoanThanh; }
    public void setNgayHoanThanh(LocalDateTime ngayHoanThanh) { this.ngayHoanThanh = ngayHoanThanh; }

    public LocalDateTime getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDateTime ngayBatDau) { this.ngayBatDau = ngayBatDau; }
}
