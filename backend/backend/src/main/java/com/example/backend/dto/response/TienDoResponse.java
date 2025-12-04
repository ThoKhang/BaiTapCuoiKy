package com.example.backend.dto.response;

public class TienDoResponse {
    private String maMonHoc;
    private String tenMonHoc;
    private int soDaHoc;
    private int tongSoBai;

    public TienDoResponse() {
    }

    public TienDoResponse(String maMonHoc, String tenMonHoc, int soDaHoc, int tongSoBai) {
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.soDaHoc = soDaHoc;
        this.tongSoBai = tongSoBai;
    }

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

    public int getSoDaHoc() {
        return soDaHoc;
    }

    public void setSoDaHoc(int soDaHoc) {
        this.soDaHoc = soDaHoc;
    }

    public int getTongSoBai() {
        return tongSoBai;
    }

    public void setTongSoBai(int tongSoBai) {
        this.tongSoBai = tongSoBai;
    }
}
