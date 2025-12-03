/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "LoaiHoatDong")
public class LoaiHoatDong {

    @Id
    @Column(name = "MaLoai", length = 5)
    private String maLoai;

    @Column(name = "TenLoai", nullable = false, unique = true, length = 50)
    private String tenLoai;

    @Column(name = "MoTaLoai", length = 100)
    private String moTaLoai;
}

