/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.dto.response;

/**
 *
 * @author ADMIN
 */
public class toanTiengVietGiaiTriResponse {
    private String tieuDe;
    private String tenMonHoc;
    private String tenLoai;

    public toanTiengVietGiaiTriResponse(String tieuDe, String tenMonHoc, String tenLoai) {
        this.tieuDe = tieuDe;
        this.tenMonHoc = tenMonHoc;
        this.tenLoai = tenLoai;
    }

    public toanTiengVietGiaiTriResponse() {
    }
    
    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

   
}
