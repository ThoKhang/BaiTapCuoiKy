package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "LuaChon")
public class LuaChon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maLuaChon;

    @Column(name = "MaCauHoi", nullable = false)
    private Integer maCauHoi;

    @Column(name = "NhanLuaChon", length = 1, nullable = false)
    private Character nhanLuaChon;

    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String noiDung;

    @Column(name = "LaDung", nullable = false)
    private Boolean laDung = false;

    // Getter và Setter
    public Integer getMaLuaChon() {
        return maLuaChon;
    }

    public void setMaLuaChon(Integer maLuaChon) {
        this.maLuaChon = maLuaChon;
    }

    public Integer getMaCauHoi() {
        return maCauHoi;
    }

    public void setMaCauHoi(Integer maCauHoi) {
        this.maCauHoi = maCauHoi;
    }

    public Character getNhanLuaChon() {
        return nhanLuaChon;
    }

    public void setNhanLuaChon(Character nhanLuaChon) {
        this.nhanLuaChon = nhanLuaChon;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Boolean getLaDung() {
        return laDung;
    }

    public void setLaDung(Boolean laDung) {
        this.laDung = laDung;
    }
}