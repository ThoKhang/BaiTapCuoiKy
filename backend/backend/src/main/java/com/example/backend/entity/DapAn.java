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
@Table(name = "DapAn")
public class DapAn {

    @Id
    @Column(name = "MaDapAn", length = 6)
    private String maDapAn;

    @ManyToOne
    @JoinColumn(name = "MaCauHoi")
    private CauHoi cauHoi;

    @Column(name = "NoiDungDapAn", nullable = false, length = 100)
    private String noiDung;

    @Column(name = "LaDapAnDung")
    private Boolean laDapAnDung;

}

