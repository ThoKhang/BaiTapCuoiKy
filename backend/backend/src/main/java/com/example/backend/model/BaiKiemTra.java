package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BaiKiemTra")
public class BaiKiemTra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maBaiKiemTra;

    @Column(name = "TieuDe", length = 200, nullable = false)
    private String tieuDe;

    @Column(name = "LoaiBaiKiemTra", length = 50, nullable = false)
    private String loaiBaiKiemTra;

    @Column(name = "MaMonHoc")
    private Byte maMonHoc;

    @Column(name = "CoXaoTron")
    private Boolean coXaoTron = true;

    @Column(name = "TongSoCauHoi")
    private Integer tongSoCauHoi = 10;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao = LocalDateTime.now();

    // Getter và Setter
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

    public Byte getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(Byte maMonHoc) {
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