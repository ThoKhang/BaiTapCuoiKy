package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DapAnCauHoiNguoiDung")
public class DapAnCauHoiNguoiDung {
    @EmbeddedId
    private DapAnKey key;

    @Column(name = "MaLuaChonChon")
    private Integer maLuaChonChon;

    @Column(name = "LaDung", nullable = false)
    private Boolean laDung;

    // Getter và Setter
    public DapAnKey getKey() {
        return key;
    }

    public void setKey(DapAnKey key) {
        this.key = key;
    }

    public Integer getMaLuaChonChon() {
        return maLuaChonChon;
    }

    public void setMaLuaChonChon(Integer maLuaChonChon) {
        this.maLuaChonChon = maLuaChonChon;
    }

    public Boolean getLaDung() {
        return laDung;
    }

    public void setLaDung(Boolean laDung) {
        this.laDung = laDung;
    }
}