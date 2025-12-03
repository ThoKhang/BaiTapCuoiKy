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
@Table(name = "CauHoi")
public class CauHoi {

    @Id
    @Column(name = "MaCauHoi", length = 5)
    private String maCauHoi;

    @Column(name = "NoiDungCauHoi", nullable = false, length = 300)
    private String noiDungCauHoi;

    @Column(name = "GiaiThich", length = 300)
    private String giaiThich;

    @Column(name = "DiemToiDa")
    private Integer diemToiDa;

}
