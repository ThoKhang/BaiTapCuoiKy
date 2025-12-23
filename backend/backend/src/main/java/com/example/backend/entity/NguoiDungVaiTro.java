package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "NguoiDung_VaiTro")
@IdClass(NguoiDungVaiTroId.class)
public class NguoiDungVaiTro {

    @Id
    @Column(name = "MaNguoiDung", length = 5)
    private String maNguoiDung;

    @Id
    @Column(name = "MaVaiTro", length = 5)
    private String maVaiTro;

    public NguoiDungVaiTro() {
    }

    public NguoiDungVaiTro(String maNguoiDung, String maVaiTro) {
        this.maNguoiDung = maNguoiDung;
        this.maVaiTro = maVaiTro;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getMaVaiTro() {
        return maVaiTro;
    }

    public void setMaVaiTro(String maVaiTro) {
        this.maVaiTro = maVaiTro;
    }
}
