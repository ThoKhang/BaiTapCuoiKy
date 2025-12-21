package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MonHoc")
public class MonHoc {

    @Id
    @Column(name = "MaMonHoc", length = 5)
    private String maMonHoc;

    @Column(name = "TenMonHoc", unique = true, nullable = false)
    private String tenMonHoc;

    // getter & setter (không Lombok)
    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }
}
