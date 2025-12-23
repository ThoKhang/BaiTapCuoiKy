package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MediaNguoiDungId implements Serializable {

    @Column(name = "MaMedia")
    private Long maMedia;

    @Column(name = "MaNguoiDung", length = 5)
    private String maNguoiDung;

    public MediaNguoiDungId() {}

    public MediaNguoiDungId(Long maMedia, String maNguoiDung) {
        this.maMedia = maMedia;
        this.maNguoiDung = maNguoiDung;
    }

    public Long getMaMedia() { return maMedia; }
    public void setMaMedia(Long maMedia) { this.maMedia = maMedia; }

    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaNguoiDungId)) return false;
        MediaNguoiDungId that = (MediaNguoiDungId) o;
        return Objects.equals(maMedia, that.maMedia) && Objects.equals(maNguoiDung, that.maNguoiDung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maMedia, maNguoiDung);
    }
}
