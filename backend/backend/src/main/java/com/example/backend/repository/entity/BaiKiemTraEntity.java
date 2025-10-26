/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "BaiKiemTra")
public class BaiKiemTraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBaiKiemTra")
    private Integer maBaiKiemTra;
    @Column(name = "TieuDe")
    private String tieuDe;
    @Column(name = "LoaiBaiKiemTra")
    private String loaiBaiKiemTra;
    @Column(name = "MaMonHoc")
    private Integer maMonHoc;
    @Column(name = "CoXaoTron")
    private Boolean coXaoTron;
    @Column(name = "TongSoCauHoi")
    private Integer tongSoCauHoi;
    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    public Integer getMaBaiKiemTra() {
        return maBaiKiemTra;
    }

    public void setMaBaiKiemTra(Integer maBaiKiemTra) {
        this.maBaiKiemTra = maBaiKiemTra;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getLoaiBaiKiemTra() {
        return loaiBaiKiemTra;
    }

    public void setLoaiBaiKiemTra(String loaiBaiKiemTra) {
        this.loaiBaiKiemTra = loaiBaiKiemTra;
    }

    public Integer getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(Integer maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public Boolean getCoXaoTron() {
        return coXaoTron;
    }

    public void setCoXaoTron(Boolean coXaoTron) {
        this.coXaoTron = coXaoTron;
    }

    public Integer getTongSoCauHoi() {
        return tongSoCauHoi;
    }

    public void setTongSoCauHoi(Integer tongSoCauHoi) {
        this.tongSoCauHoi = tongSoCauHoi;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
    
}
