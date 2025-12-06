/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "LoaiHoatDong")
public class LoaiHoatDong {

    @Id
    @Column(name = "MaLoai", length = 5)
    private String maLoai;

    @Column(name = "TenLoai", nullable = false, unique = true, length = 50)
    private String tenLoai;

    @Column(name = "MoTaLoai", length = 100)
    private String moTaLoai;

    public LoaiHoatDong(String maLoai, String tenLoai, String moTaLoai) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.moTaLoai = moTaLoai;
    }

    public LoaiHoatDong() {
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getMoTaLoai() {
        return moTaLoai;
    }

    public void setMoTaLoai(String moTaLoai) {
        this.moTaLoai = moTaLoai;
    }
    
}