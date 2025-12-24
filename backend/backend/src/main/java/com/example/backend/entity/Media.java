package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaMedia")
    private Long maMedia;

    @Column(name = "TieuDe", nullable = false, length = 200)
    private String tieuDe;

    @Column(name = "MoTa", length = 1000)
    private String moTa;

    @Column(name = "LoaiMedia", nullable = false, length = 10)
    private String loaiMedia; // VIDEO | AUDIO

    @Column(name = "DuongDanFile", nullable = false, length = 500)
    private String duongDanFile; // ví dụ: uploads/videos/abc.mp4

    @Column(name = "ThoiLuongGiay")
    private Integer thoiLuongGiay;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @PrePersist
    public void prePersist() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
    }

    public Long getMaMedia() { return maMedia; }
    public void setMaMedia(Long maMedia) { this.maMedia = maMedia; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getLoaiMedia() { return loaiMedia; }
    public void setLoaiMedia(String loaiMedia) { this.loaiMedia = loaiMedia; }

    public String getDuongDanFile() { return duongDanFile; }
    public void setDuongDanFile(String duongDanFile) { this.duongDanFile = duongDanFile; }

    public Integer getThoiLuongGiay() { return thoiLuongGiay; }
    public void setThoiLuongGiay(Integer thoiLuongGiay) { this.thoiLuongGiay = thoiLuongGiay; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
}
