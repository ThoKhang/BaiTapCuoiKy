package com.example.backend.model;

// === MODEL HIỂN THỊ CÂU HỎI CỦNG CỐ ===
class CauHoiResponse {
    private int maCauHoi;
    private String noiDung;
    private int maLuaChon;
    private String nhanLuaChon;
    private String noiDungLuaChon;
    private boolean laDung;

    public int getMaCauHoi() { return maCauHoi; }
    public void setMaCauHoi(int maCauHoi) { this.maCauHoi = maCauHoi; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public int getMaLuaChon() { return maLuaChon; }
    public void setMaLuaChon(int maLuaChon) { this.maLuaChon = maLuaChon; }

    public String getNhanLuaChon() { return nhanLuaChon; }
    public void setNhanLuaChon(String nhanLuaChon) { this.nhanLuaChon = nhanLuaChon; }

    public String getNoiDungLuaChon() { return noiDungLuaChon; }
    public void setNoiDungLuaChon(String noiDungLuaChon) { this.noiDungLuaChon = noiDungLuaChon; }

    public boolean isLaDung() { return laDung; }
    public void setLaDung(boolean laDung) { this.laDung = laDung; }
}
