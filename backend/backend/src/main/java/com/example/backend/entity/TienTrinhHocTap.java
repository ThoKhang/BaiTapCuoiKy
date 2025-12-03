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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author ADMIN
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}

