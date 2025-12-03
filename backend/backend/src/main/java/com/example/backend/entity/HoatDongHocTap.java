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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
