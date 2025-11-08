package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "BaiKiemTraCauHoi")
public class BaiKiemTraCauHoi {
    @EmbeddedId
    private BaiKiemTraCauHoiKey key;

    @Column(name = "ThuTu", nullable = false)
    private Byte thuTu;

    // Getter và Setter
    public BaiKiemTraCauHoiKey getKey() {
        return key;
    }

    public void setKey(BaiKiemTraCauHoiKey key) {
        this.key = key;
    }

    public Byte getThuTu() {
        return thuTu;
    }

    public void setThuTu(Byte thuTu) {
        this.thuTu = thuTu;
    }
}