package com.example.apphoctapchotre.DATA.model;


public class LuaChon {
    private int maLuaChon;
    private char nhanLuaChon;
    private String noiDung;
    private boolean laDung;

    // Getter và Setter
    public int getMaLuaChon() {
        return maLuaChon;
    }

    public void setMaLuaChon(int maLuaChon) {
        this.maLuaChon = maLuaChon;
    }

    public char getNhanLuaChon() {
        return nhanLuaChon;
    }

    public void setNhanLuaChon(char nhanLuaChon) {
        this.nhanLuaChon = nhanLuaChon;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public boolean isLaDung() {
        return laDung;
    }

    public void setLaDung(boolean laDung) {
        this.laDung = laDung;
    }
}