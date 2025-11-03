package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CauHoi")
public class CauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maCauHoi;

    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String noiDung;

    @Column(name = "MaMonHoc")
    private Byte maMonHoc;

    @Column(name = "GiaiThich", columnDefinition = "NVARCHAR(MAX)")
    private String giaiThich;

    // Getter và Setter
    public Integer getMaCauHoi() {
        return maCauHoi;
    }

    public void setMaCauHoi(Integer maCauHoi) {
        this.maCauHoi = maCauHoi;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Byte getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(Byte maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public String getGiaiThich() {
        return giaiThich;
    }

    public void setGiaiThich(String giaiThich) {
        this.giaiThich = giaiThich;
    }
}