package com.example.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Media_NguoiDung")
public class MediaNguoiDung {

    @EmbeddedId
    private MediaNguoiDungId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maMedia")
    @JoinColumn(name = "MaMedia", nullable = false)
    private Media media;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maNguoiDung")
    @JoinColumn(name = "MaNguoiDung", nullable = false)
    private NguoiDung nguoiDung;

    @Column(name = "DaXem", nullable = false)
    private boolean daXem;

    @Column(name = "ViTriGiay", nullable = false)
    private int viTriGiay;

    @Column(name = "LanXemCuoi")
    private LocalDateTime lanXemCuoi;

    @PrePersist
    public void prePersist() {
        if (lanXemCuoi == null) lanXemCuoi = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        lanXemCuoi = LocalDateTime.now();
    }

    public MediaNguoiDungId getId() { return id; }
    public void setId(MediaNguoiDungId id) { this.id = id; }

    public Media getMedia() { return media; }
    public void setMedia(Media media) { this.media = media; }

    public NguoiDung getNguoiDung() { return nguoiDung; }
    public void setNguoiDung(NguoiDung nguoiDung) { this.nguoiDung = nguoiDung; }

    public boolean isDaXem() { return daXem; }
    public void setDaXem(boolean daXem) { this.daXem = daXem; }

    public int getViTriGiay() { return viTriGiay; }
    public void setViTriGiay(int viTriGiay) { this.viTriGiay = viTriGiay; }

    public LocalDateTime getLanXemCuoi() { return lanXemCuoi; }
    public void setLanXemCuoi(LocalDateTime lanXemCuoi) { this.lanXemCuoi = lanXemCuoi; }
}
