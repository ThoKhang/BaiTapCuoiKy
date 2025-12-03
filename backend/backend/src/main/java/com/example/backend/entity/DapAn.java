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

    public DapAn() {
    }

    public DapAn(String maDapAn, CauHoi cauHoi, String noiDung, Boolean laDapAnDung) {
        this.maDapAn = maDapAn;
        this.cauHoi = cauHoi;
        this.noiDung = noiDung;
        this.laDapAnDung = laDapAnDung;
    }

    public String getMaDapAn() {
        return maDapAn;
    }

    public void setMaDapAn(String maDapAn) {
        this.maDapAn = maDapAn;
    }

    public CauHoi getCauHoi() {
        return cauHoi;
    }

    public void setCauHoi(CauHoi cauHoi) {
        this.cauHoi = cauHoi;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Boolean getLaDapAnDung() {
        return laDapAnDung;
    }

    public void setLaDapAnDung(Boolean laDapAnDung) {
        this.laDapAnDung = laDapAnDung;
    }
    

}

